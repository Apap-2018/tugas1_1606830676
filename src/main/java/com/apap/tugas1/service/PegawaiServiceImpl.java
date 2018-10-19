package com.apap.tugas1.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.JabatanPegawaiModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.InstansiDb;
import com.apap.tugas1.repository.JabatanDb;
import com.apap.tugas1.repository.JabatanPegawaiDb;
import com.apap.tugas1.repository.PegawaiDb;

@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService{
	
	@Autowired
	private PegawaiDb pegawaiDb;
	
	@Autowired
	private JabatanDb jabatanDb;
	
	@Autowired
	private InstansiDb instansiDb;

	@Override
	public void addPegawaiBaru(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
	}

	@Override
	public void hapusPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
	}

	@Override
	public void ubahPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
		
	}

	@Override
	public Optional<PegawaiModel> getPegawaiDetailById(Long id) {
		return pegawaiDb.findById(id);
	}

	@Override
	public PegawaiModel getPegawaiByNip(String nip) {
		return pegawaiDb.findByNip(nip);
		
	}

	@Override
	public List<PegawaiModel> findByTahunMasukAndInstansi(String tahunMasuk, InstansiModel instansi ){
		return pegawaiDb.findByTahunMasukAndInstansi(tahunMasuk, instansi);
	}
	
	@Override
	public void deleteListElement(List<PegawaiModel> listPegawai, int tglLahir) {
		Iterator<PegawaiModel> i = listPegawai.iterator();
		while (i.hasNext()) {
			PegawaiModel peg = i.next();
			if(Integer.parseInt(peg.getTanggalLahirStr()) != tglLahir) {
				i.remove();
			} 
		}
	}

	@Override
	public void ubahPegawai(String nip, PegawaiModel pegawai) {
		PegawaiModel oldPegawai = this.getPegawaiByNip(nip);
		oldPegawai.setInstansi(pegawai.getInstansi());
		oldPegawai.setNama(pegawai.getNama());
		oldPegawai.setNip(pegawai.getNip());
		oldPegawai.setTahunMasuk(pegawai.getTahunMasuk());
		oldPegawai.setTanggalLahir(pegawai.getTanggalLahir());
		oldPegawai.setTempatLahir(pegawai.getTempatLahir());
		oldPegawai.setJabatanList(pegawai.getJabatanList());
	}

	@Override
	public List<PegawaiModel> findByInstansi(InstansiModel instansi) {
		List<PegawaiModel> listPegawai = pegawaiDb.findAllByInstansi(instansi);
		return listPegawai;
	}
	
	
	@Override
	public List<PegawaiModel> getFilter(String idInstansi, String idJabatan) {
		List<PegawaiModel> list = new ArrayList<PegawaiModel>();
		InstansiModel instansi = instansiDb.findInstansiById(Long.parseLong(idInstansi));
		List<PegawaiModel> listPegawai = pegawaiDb.findAllByInstansi(instansi);
		JabatanModel jabatan = jabatanDb.findJabatanById(Long.parseLong(idJabatan));
		for(PegawaiModel pegawai : listPegawai) {
			for(JabatanModel jabatanA : pegawai.getJabatanList()) {
				if(jabatanA.getId() == Long.parseLong(idJabatan)) {
					list.add(pegawai);
				}
			}
		}
		return list;
	}
	

	
	@Override
	public int findJabatanList(List<JabatanModel> listJabatan, Long id) {
		int ret = 0;
		Iterator<JabatanModel> i = listJabatan.iterator();
		while (i.hasNext()) {
			JabatanModel jabatan = i.next();
			if(jabatan.getId() == id) {
				ret++;
			} 
		}
		return ret;
	}
}