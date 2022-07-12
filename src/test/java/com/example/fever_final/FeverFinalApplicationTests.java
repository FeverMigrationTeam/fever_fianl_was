package com.example.fever_final;

import com.example.fever_final.table.regala.entity.Regala;
import com.example.fever_final.table.regala.etc.RegalaStatus;

import com.example.fever_final.table.regala.repository.RegalaRepository;
import com.example.fever_final.table.stadium.entity.Stadium;
import com.example.fever_final.table.stadium.repository.StadiumRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class FeverFinalApplicationTests {


    @Autowired
    private RegalaRepository regalaRepository;

    @Autowired
    private StadiumRepository stadiumRepository;


    @Test
    void contextLoads() {


        Optional<Stadium> byStadiumName = stadiumRepository.findByStadiumName("다산구장");
        Stadium stadium = byStadiumName.get();

        Regala regala = Regala.buildRegala(stadium, RegalaStatus.AVAILABLE);

        regalaRepository.save(regala);
//
//        Optional<Regala> byId = regalaRepository.findById(3L);
//        Regala regala = byId.get();
//
//        if (regala.getRegalaStatus().equals(RegalaStatus.AVAILABLE))
//            regala.setRegalaStatus(RegalaStatus.INUSE);
//
//        regalaRepository.save(regala);


    }

}
