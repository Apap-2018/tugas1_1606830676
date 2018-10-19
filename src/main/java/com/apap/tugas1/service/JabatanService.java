package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	void addJabatan (JabatanModel jabatan);
	void hapusJabatan (JabatanModel jabatan);
	void ubahJabatan (JabatanModel jabatan);
	
	JabatanModel getJabatanDetailById(Long id);
	List<JabatanModel> findAllJabatan();
	
	void ubahJabatan(Long id_jabatan, JabatanModel jabatan);
}