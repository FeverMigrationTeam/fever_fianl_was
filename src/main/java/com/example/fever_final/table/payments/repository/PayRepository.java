package com.example.fever_final.table.payments.repository;


import com.example.fever_final.table.payments.entity.PayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayRepository extends JpaRepository<PayInfo, Long> {
    Optional<PayInfo> findByUserId(Long userId);
}
