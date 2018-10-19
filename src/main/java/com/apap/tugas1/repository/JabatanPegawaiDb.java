package com.apap.tugas1.repository;

import com.apap.tugas1.model.JabatanPegawaiModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JabatanPegawaiDb extends JpaRepository<JabatanPegawaiModel, Long> {
}