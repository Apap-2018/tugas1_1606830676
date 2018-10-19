package com.apap.tugas1.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.*;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	@RequestMapping("/")
	private String home(Model model) {
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		model.addAttribute("listOfJabatan", daftarJabatan);
		return "HomePage";
	}
	
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	private String findPegawai(@RequestParam(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiByNip(nip);
		if (pegawai!=null ) {	
			model.addAttribute("pegawai", pegawai);
			return "view-pegawaiProfile";
		}
		return "not-foundError";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("pegawai", new PegawaiModel());
		
		List<ProvinsiModel> daftarProvinsi = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProvinsi);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
		return "addPegawaiBaru";
	}

	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPegawaiSubmit(@RequestParam("instansi") String idInstansi, @RequestParam("provinsi") String idProvinsi, 
			@RequestParam("jabatan") String idJabatan, @ModelAttribute PegawaiModel pegawai, Model model) {
		System.out.println(pegawai.getNama());
		InstansiModel instansi = instansiService.findById(Long.parseLong(idInstansi));
		List<String> idJabatanList = new ArrayList<String>(Arrays.asList(idJabatan.split(",")));
		List<JabatanModel> jabatanList = new ArrayList<JabatanModel>();
		for(String str : idJabatanList) {
			JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(str));
			jabatanList.add(jabatan);
		}
		
		pegawai.setJabatanList(jabatanList);
		pegawai.setInstansi(instansi);
		
		System.out.println(pegawai.getTanggalLahirStr());
		System.out.println(instansi.getId());
		
		String nip = instansi.getId() + pegawai.getTanggalLahirStr() + pegawai.getTahunMasuk();

		List<PegawaiModel> otherPegawai = pegawaiService.findByTahunMasukAndInstansi(pegawai.getTahunMasuk(), instansi);

		if(otherPegawai.isEmpty()) {
			nip += "01";
		}
		else {
			pegawaiService.deleteListElement(otherPegawai, Integer.parseInt(pegawai.getTahunLahir()));//trace dan buang pegawai tahun lahir yang tidak sama dari list pegawai
			otherPegawai.sort(new Comparator<PegawaiModel>() { //urutkan descending pegawai tahun lahir sama based on NIP
			    @Override
			    public int compare(PegawaiModel m1, PegawaiModel m2) {
			    	return m2.getNip().compareTo(m1.getNip());
			     }
			});
			System.out.println(otherPegawai.get(0).getNip());
			/*mengambil nip dari pegawai teratas, dan mengambil nipnya, lalu tambahkan 1 dari nip tersebut dan
			ambil 2 digit terakhir dari nip yang sudah ditambahkan, untuk ditaruh di nip pegawai baru */
			Long number = Long.parseLong(otherPegawai.get(0).getNip());
			number += 1;
			nip += Long.toString(number).substring(14);
		}
		
		pegawai.setNip(nip);
		System.out.println(pegawai.toString());
		pegawaiService.addPegawaiBaru(pegawai);
		model.addAttribute("nip", nip);
		return "addPegawai-success";
	}
	

	@RequestMapping(value = "/pegawai/ubah/{nip}", method = RequestMethod.GET)
	private String updatePegawai(@PathVariable(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiByNip(nip);
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("newPegawai", new PegawaiModel());
		
		List<ProvinsiModel> daftarProv = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProv);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
	    
		return "ubahPegawai";
	}

	@RequestMapping(value = "/pegawai/ubah/{nip}", method = RequestMethod.POST)
	private String updatePegawai(@ModelAttribute PegawaiModel pegawaiBaru, @RequestParam("instansiA") String idInstansi, @RequestParam("provinsi") String idProvinsi, 
			@RequestParam("jabatan") String idJabatan, @PathVariable(value = "nip") String nip, Model model) {
		System.out.println(nip);
		InstansiModel instansiBaru = instansiService.findById(Long.parseLong(idInstansi));
		System.out.println(instansiBaru.getNama());
	
		List<String> newIdJabatanList = new ArrayList<String>(Arrays.asList(idJabatan.split(",")));
		List<JabatanModel> newJabatanList = new ArrayList<JabatanModel>();
		for(String str : newIdJabatanList) {
			JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(str));
			newJabatanList.add(jabatan);
		}
		pegawaiBaru.setJabatanList(newJabatanList);
		pegawaiBaru.setInstansi(instansiBaru);
		System.out.println(pegawaiBaru.getInstansi().getNama()+"ini");
		
		String nipBaru = instansiBaru.getId() + pegawaiBaru.getTanggalLahirStr() + pegawaiBaru.getTahunMasuk();
		
		
		List<PegawaiModel> newAnotherPegawai = pegawaiService.findByTahunMasukAndInstansi(pegawaiBaru.getTahunMasuk(), instansiBaru);
		if(newAnotherPegawai.isEmpty()) {
			nipBaru += "01";
		}
		else {
			pegawaiService.deleteListElement(newAnotherPegawai, Integer.parseInt(pegawaiBaru.getTahunLahir()));
			newAnotherPegawai.sort(new Comparator<PegawaiModel>() {
			    @Override
			    public int compare(PegawaiModel m1, PegawaiModel m2) {
			    	return m2.getNip().compareTo(m1.getNip());
			     }
			});
			System.out.println(newAnotherPegawai.get(0).getNip());
			
			Long number = Long.parseLong(newAnotherPegawai.get(0).getNip()); /*ambil nip teratas, tambah 1, ambil 2 digit terakhir letakkan di nip pegawai baru */
			number += 1;
			nipBaru += Long.toString(number).substring(14);
		}
		
		pegawaiBaru.setNip(nipBaru);
		PegawaiModel p = pegawaiService.getPegawaiByNip(nip);
		System.out.println(p.getNip()+p.getNama());
		pegawaiService.ubahPegawai(nip, pegawaiBaru);
		model.addAttribute("nip", nipBaru);
		return "ubahPegawai-success";
	}
	
	@RequestMapping(value = "/pegawai/cari", method = RequestMethod.GET)
	private String findPegawai(Model model) {
		List<ProvinsiModel> daftarProv = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProv);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
		return "cari-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/cari", params = {"cari"}, method = RequestMethod.POST)
	private String findPegawaiCari(@ModelAttribute PegawaiModel newPegawai, @RequestParam("instansi") String idInstansi, @RequestParam("provinsi") String idProvinsi, 
			@RequestParam("jabatan") String idJabatan, Model model) {
		System.out.println("aaa");
		InstansiModel instansi = instansiService.findById(Long.parseLong(idInstansi));
		List<PegawaiModel> listPegawai_tmp = pegawaiService.findByInstansi(instansi);
		List<PegawaiModel> listPegawai = new ArrayList<PegawaiModel>();
		for(PegawaiModel pegawai : listPegawai_tmp) {
			if(pegawaiService.findJabatanList(pegawai.getJabatanList(), Long.parseLong(idJabatan)) > 0){
				listPegawai.add(pegawai);
			}
			else {
				return "not-found";
			}
		}
		model.addAttribute("listPegawai" , listPegawai);
		model.addAttribute("namaJabatan" , jabatanService.getJabatanDetailById(Long.parseLong(idJabatan)));
		return "cari-pegawai";
	}
	
	
}