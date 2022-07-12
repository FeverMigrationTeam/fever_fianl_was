package com.example.fever_final.table.stadium.repository;


import com.example.fever_final.table.stadium.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium,Long> {

    Optional<Stadium> findByStadiumName(String name);
}
