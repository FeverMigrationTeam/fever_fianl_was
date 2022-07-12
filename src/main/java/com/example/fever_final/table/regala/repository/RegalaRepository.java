package com.example.fever_final.table.regala.repository;

import com.example.fever_final.table.regala.entity.Regala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegalaRepository extends JpaRepository<Regala,Long> {

}
