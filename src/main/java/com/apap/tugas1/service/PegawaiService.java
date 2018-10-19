package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;


public interface PegawaiService {
	void addPegawaiBaru (PegawaiModel pegawai);
	void hapusPegawai (PegawaiModel pegawai);
	void ubahPegawai (PegawaiModel pegawai);
	
	Optional<PegawaiModel> getPegawaiDetailById(Long id);
	PegawaiModel getPegawaiByNip(String nip);
	List<PegawaiModel> findByTahunMasukAndInstansi(String tahunMasuk, InstansiModel instansi );
	
	void ubahPegawai(String nip, PegawaiModel pegawai);
	
	List<PegawaiModel> findByInstansi(InstansiModel instansi);
	int findJabatanList(List<JabatanModel> listJabatan, Long id);
	List<PegawaiModel> getFilter(String idInstansi, String idJabatan);
	void deleteListElement(List<PegawaiModel> listPegawai, int tahunLahir);
}