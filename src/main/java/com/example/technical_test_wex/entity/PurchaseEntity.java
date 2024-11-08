package com.example.technical_test_wex.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TbPurchase")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Setter(AccessLevel.NONE)
    @Column(name = "IdPurchase")
    private UUID idPurchase;

    @Column(name = "DescriptionPurchase", length = 50, nullable = false)
    private String description;

    @Column(name = "TransactionDatePurchase", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "AmountPurchase", nullable = false)
    private BigDecimal purchaseAmount;
}
