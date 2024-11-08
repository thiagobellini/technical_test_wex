package com.example.technical_test_wex.repository;

import com.example.technical_test_wex.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IPurchaseRepository extends JpaRepository<PurchaseEntity, UUID> {
}
