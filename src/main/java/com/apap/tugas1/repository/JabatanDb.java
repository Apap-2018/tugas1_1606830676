package com.apap.tugas1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanDb extends JpaRepository<JabatanModel, Long>{
	JabatanModel findJabatanById(long id);
}