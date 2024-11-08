package com.example.technical_test_wex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PurchaseExchangeDTO extends RepresentationModel<PurchaseExchangeDTO> {

    private UUID idPurchase;
    private String description;
    private LocalDate transactionDate;
    private BigDecimal purchaseAmountOriginal;
    private BigDecimal exchangeRate;
    private BigDecimal purchaseAmountExchanged;
}
