package com.apap.tugas1.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.apap.tugas1.model.*;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.InstansiService;
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
public class JabatanController {
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private PegawaiService pegawaiService;
	
	@RequestMapping(value = "/jabatan", method = RequestMethod.GET)
	private String findJabatan(@RequestParam("jabatan") Long id, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(id);
		if (jabatan!=null ) {
			model.addAttribute("jabatan", jabatan);
			return "view-jabatan";
		}
		return "not-foundError";
	}
	
	@RequestMapping(value = "/jabatan/viewall", method = RequestMethod.GET)
    private String hapusJabatan(Model model) {
		List<JabatanModel> listJabatan = jabatanService.findAllJabatan();
        model.addAttribute("listJabatan", listJabatan);
        return "view-all-jabatan";
    }
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("jabatan", new JabatanModel());
		return "tambah-jabatanBaru";
	}

	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	private String addPegawaiSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.addJabatan(jabatan);
		model.addAttribute("nama", jabatan.getNama());
		return "addJabatan-success";
	}
	
	@RequestMapping(value = "/jabatan/ubah/{id_jabatan}", method = RequestMethod.GET)
	private String ubahJabatan(@PathVariable(value = "id_jabatan") String id_jabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(id_jabatan));
		model.addAttribute("jabatan", jabatan);
		model.addAttribute("newJabatan", new JabatanModel());
	    
		return "ubahJabatan";
	}

	@RequestMapping(value = "/jabatan/ubah/{id_jabatan}", method = RequestMethod.POST)
	private String ubahPegawai(@ModelAttribute JabatanModel newJabatan, 
			@PathVariable(value = "id_jabatan") String id_jabatan, Model model) {
		jabatanService.ubahJabatan(Long.parseLong(id_jabatan), newJabatan);
		model.addAttribute("nama", newJabatan.getNama());
		return "ubahJabatan-success";
	}
	
	@RequestMapping(value = "/jabatan/hapus/{id_jabatan}", method = RequestMethod.GET)
    private String hapusJabatan(@PathVariable(value = "id_jabatan") String id_jabatan, Model model) {
        JabatanModel jabatan = jabatanService.getJabatanDetailById(Long.parseLong(id_jabatan));
        List<PegawaiModel> listPegawai = jabatan.getPegawaiList();
        if(listPegawai.isEmpty()) {
        	jabatanService.hapusJabatan(jabatan);
        	model.addAttribute("nama", jabatan.getNama());
        	return "hapusJabatan";
        }
        model.addAttribute("nama", jabatan.getNama());
        return "hapusFailed";
        }
}