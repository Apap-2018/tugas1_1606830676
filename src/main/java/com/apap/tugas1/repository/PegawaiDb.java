package com.apap.tugas1.repository;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.PegawaiModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PegawaiDb extends JpaRepository<PegawaiModel,Long> {
	PegawaiModel findByNip(String nip);
	List<PegawaiModel> findByTahunMasukAndInstansi(@Param("tahun_masuk") String tahunMasuk, InstansiModel instansi );
	List<PegawaiModel> findByInstansi(InstansiModel instansi);
	List<PegawaiModel> findAllByInstansi(InstansiModel instansi);
}