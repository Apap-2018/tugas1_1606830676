package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.JabatanPegawaiModel;

public interface JabatanPegawaiService {
	void addJabatanPegawai (JabatanPegawaiModel jabatan);
	void hapusJabatanPegawai (JabatanPegawaiModel jabatan);
	void ubahJabatanPegawai (JabatanPegawaiModel jabatan);
	Optional<JabatanPegawaiModel> getJabatanPegawaiDetailById(Long id);
}