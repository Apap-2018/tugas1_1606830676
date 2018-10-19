package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.ProvinsiModel;

public interface InstansiService {
	void addInstansi (InstansiModel instansi);
	void deleteInstansi (InstansiModel instansi);
	void updateInstansi (InstansiModel instansi);
	InstansiModel findById(Long id);
	InstansiModel getInstansiByNamaAndProvinsi(String nama, ProvinsiModel provinsi);
	List<InstansiModel> findAllInstansi(); 
}