package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.JabatanPegawaiModel;
import com.apap.tugas1.repository.JabatanPegawaiDb;

@Service
@Transactional
public class JabatanPegawaiServiceImpl implements JabatanPegawaiService{
	
	@Autowired
	private JabatanPegawaiDb jabatanPegawaiDb;

	@Override
	public void addJabatanPegawai(JabatanPegawaiModel jabatan) {
		jabatanPegawaiDb.save(jabatan);
		
	}

	@Override
	public void hapusJabatanPegawai(JabatanPegawaiModel jabatan) {
		jabatanPegawaiDb.delete(jabatan);
		
	}

	@Override
	public void ubahJabatanPegawai(JabatanPegawaiModel jabatan) {
		jabatanPegawaiDb.save(jabatan);
		
	}

	@Override
	public Optional<JabatanPegawaiModel> getJabatanPegawaiDetailById(Long id) {
		return jabatanPegawaiDb.findById(id);
	}


}