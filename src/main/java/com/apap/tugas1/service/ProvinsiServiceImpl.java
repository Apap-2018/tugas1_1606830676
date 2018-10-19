package com.apap.tugas1.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.repository.ProvinsiDb;

@Service
@Transactional
public class ProvinsiServiceImpl implements ProvinsiService{
	
	@Autowired
	private ProvinsiDb prov;

	@Override
	public ProvinsiModel getProvinsiDetailById(int id) {
		return prov.findById(id);
	}

	@Override
	public List<ProvinsiModel> findAllProvinsi() {
		return prov.findAll();
	}

}