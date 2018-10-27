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
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		model.addAttribute("listOfJabatan", daftarJabatan);
		model.addAttribute("listOfInstansi", daftarInstansi);
		return "HomePage";
	}
	
	@RequestMapping(value = "/pegawai", method = RequestMethod.GET)
	private String findPegawai(@RequestParam(value = "nip") String nip, Model model) {
		PegawaiModel siPegawai = pegawaiService.getPegawaiByNip(nip);
		if (siPegawai!=null) {	
			model.addAttribute("pegawai", siPegawai);
			return "view-pegawaiProfile";
		}
		return "not-foundError";
	}
	
	//PROSES ADDING PEGAWAGAI BARU
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("pegawai", new PegawaiModel());
		List<ProvinsiModel> daftarProvinsi = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProvinsi);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
		return "tambahPegawaiBaru";
	
	}

	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPegawaiSubmit(@RequestParam("instansi") String idInstansi, @RequestParam("provinsi") String idProvinsi, 
		@RequestParam("jabatan") String idJabatan, @ModelAttribute PegawaiModel pegawai, Model model) {
		
		InstansiModel instansiPegawai = instansiService.findById(Long.parseLong(idInstansi));
		List<String> idJabatanList = new ArrayList<String>(Arrays.asList(idJabatan.split(",")));
		List<JabatanModel> jabatanList = new ArrayList<JabatanModel>();
		
		for(String str : idJabatanList) {
			JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(str));
			jabatanList.add(jabatan);
		}
		
		pegawai.setJabatanList(jabatanList);
		pegawai.setInstansi(instansiPegawai);
		
		String nip = instansiPegawai.getId() + pegawai.getTanggalLahirStr() + pegawai.getTahunMasuk();

		List<PegawaiModel> otherPegawai = pegawaiService.findPegByThnMskDanInst(pegawai.getTahunMasuk(), instansiPegawai);

		if(otherPegawai.isEmpty()) {
			nip += "01";
		}
		
		else {
			pegawaiService.deleteListElement(otherPegawai, Integer.parseInt(pegawai.getTahunLahir()));//trace dan buang pegawai tahun lahir yang tidak sama dari list pegawai
			otherPegawai.sort(new Comparator<PegawaiModel>() { //urutkan descending pegawai tahun lahir sama based on NIP
			    @Override
			    public int compare(PegawaiModel pegawai1, PegawaiModel pegawai2) {
			    	return pegawai2.getNip().compareTo(pegawai1.getNip());
			     }
			});
			
			Long number = Long.parseLong(otherPegawai.get(0).getNip()); //ambil nip dari pegawai teratas, tambahkan 1,ambil 2 digit terakhir letakkan pada nip pegawai baru
			number += 1;
			nip += Long.toString(number).substring(14);
		}
		
		pegawai.setNip(nip);
		pegawaiService.addPegawaiBaru(pegawai);
		model.addAttribute("nip", nip);
		
		return "tambahPegawai-success";
	}
	
	//PROSES UPDATE DATA PEGAWAI
	@RequestMapping(value = "/pegawai/ubah/{nip}", method = RequestMethod.GET)
	private String ubahPegawai(@PathVariable(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiByNip(nip);
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("newPegawai", new PegawaiModel());
		
		List<ProvinsiModel> daftarProvinsi = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProvinsi);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
	    
		return "ubahPegawai";
	}
	
	
	@RequestMapping(value = "/pegawai/ubah/{nip}", method = RequestMethod.POST)
	private String updatePegawaiSubmit(@ModelAttribute PegawaiModel pegawaiBaru, @RequestParam("instansiA") String idInstansi,
		@RequestParam("provinsi") String idProvinsi, @RequestParam("jabatan") String idJabatan,
		@PathVariable(value = "nip") String nip, Model model) {
		InstansiModel instansiBaru = instansiService.findById(Long.parseLong(idInstansi));
	
		List<String> idJabatanNewList = new ArrayList<String>(Arrays.asList(idJabatan.split(",")));
		List<JabatanModel> listJabatanBaru = new ArrayList<JabatanModel>();
		
		for(String str : idJabatanNewList) {
			JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(str));
			listJabatanBaru.add(jabatan);
		}
		
		pegawaiBaru.setJabatanList(listJabatanBaru);
		pegawaiBaru.setInstansi(instansiBaru);
		
		String nipBaru = instansiBaru.getId() + pegawaiBaru.getTanggalLahirStr() + pegawaiBaru.getTahunMasuk();
		
		List<PegawaiModel> otherPegawaiBaru = pegawaiService.findPegByThnMskDanInst(pegawaiBaru.getTahunMasuk(), instansiBaru);
		
		if(otherPegawaiBaru.isEmpty()) {
			nipBaru += "01";
		}
		else {
			pegawaiService.deleteListElement(otherPegawaiBaru, Integer.parseInt(pegawaiBaru.getTahunLahir()));
			otherPegawaiBaru.sort(new Comparator<PegawaiModel>() {
			    @Override
			    public int compare(PegawaiModel pegawai1, PegawaiModel pegawai2) {
			    	return pegawai2.getNip().compareTo(pegawai1.getNip());
			     }
			});
			
			Long numb = Long.parseLong(otherPegawaiBaru.get(0).getNip()); /*ambil nip teratas, tambah 1, ambil 2 digit terakhir letakkan di nip pegawai baru */
			numb += 1;
			nipBaru += Long.toString(numb).substring(14);
		}
		
		pegawaiBaru.setNip(nipBaru);
		PegawaiModel p = pegawaiService.getPegawaiByNip(nip);
		pegawaiService.ubahPegawai(nip, pegawaiBaru);
		model.addAttribute("nip", nipBaru);
		
		return "ubahPegawai-success";
	}
	
	
	//PROSES PENCARIAN PEGAWAI
	@RequestMapping(value = "/pegawai/cari", method = RequestMethod.GET)
	private String cariPegawai(Model model) {
		
		
		List<ProvinsiModel> daftarProvinsi = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProvinsi);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
	    
		return "cari-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/cari", params = {"cari"}, method = RequestMethod.POST)
	private String cariPegawaiSubmit(@ModelAttribute PegawaiModel newPegawai, @RequestParam("instansi") String idInstansi,
		@RequestParam("provinsi") String idProvinsi, @RequestParam("jabatan") String idJabatan, Model model) {
		
		InstansiModel instansi = instansiService.findById(Long.parseLong(idInstansi));
		JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(idJabatan));
		List<PegawaiModel> listPegawai = pegawaiService.getFilter(idInstansi, idJabatan);
		
		model.addAttribute("nama", instansi.getNama());
		model.addAttribute("namaJabatan", jabatan.getNama());
	    model.addAttribute("listPegawai", listPegawai);
	    
	    List<ProvinsiModel> daftarProvinsi = provinsiService.findAllProvinsi();
		List<JabatanModel> daftarJabatan = jabatanService.findAllJabatan();
		List<InstansiModel> daftarInstansi = instansiService.findAllInstansi();
		
	    model.addAttribute("listOfProvinsi", daftarProvinsi);
	    model.addAttribute("listOfJabatan", daftarJabatan);
	    model.addAttribute("listOfInstansi", daftarInstansi);
	    
		return "cari-pegawai";
	}
	
	//PROSES MENCARI PEGAWAI TERMUDA TERTUA
	@RequestMapping(value = "/pegawai/termuda-tertua", method = RequestMethod.GET)
	private String findPegawaiTermudaTertua(@RequestParam("instansi")Long id, @ModelAttribute PegawaiModel newPegawai, Model model) {
		InstansiModel instansi = instansiService.findById(id);
		List<PegawaiModel> listPegawai = instansi.getPegawaiInstansi();
		listPegawai.sort(new Comparator<PegawaiModel>() {
			
			@Override
			public int compare(PegawaiModel pegawai1, PegawaiModel pegawai2) {
				return pegawai2.getTanggalLahir().compareTo(pegawai1.getTanggalLahir());
			}
		});
		
		PegawaiModel oldest = listPegawai.get(listPegawai.size()-1);
		String oldestInstansi = oldest.getInstansi().getNama();
		String oldestProvinsi = oldest.getInstansi().getProvinsi().getNama();
		List<JabatanModel> oldestListJabatan = oldest.getJabatanList();
		
		model.addAttribute("oldest", oldest);
		model.addAttribute("oldestInstansi", oldestInstansi);
		model.addAttribute("oldestProvinsi", oldestProvinsi);
		model.addAttribute("oldestListJabatan", oldestListJabatan);
		
		PegawaiModel youngest = listPegawai.get(0);
		String youngestInstansi = youngest.getInstansi().getNama();
		String youngestProvinsi = youngest.getInstansi().getProvinsi().getNama();
		List<JabatanModel> youngestListJabatan = youngest.getJabatanList();
		
		model.addAttribute("youngest", youngest);
		model.addAttribute("youngestInstansi", youngestInstansi);
		model.addAttribute("youngestProvinsi", youngestProvinsi);
		model.addAttribute("youngestListJabatan", youngestListJabatan);
		
		return "termuda-tertua";
	}

}