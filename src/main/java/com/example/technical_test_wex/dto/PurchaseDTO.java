package com.example.technical_test_wex.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class PurchaseDTO extends RepresentationModel<PurchaseDTO> {

    private UUID idPurchase;

    @NotNull
    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;

    @NotNull
    @PastOrPresent(message = "The date of the transaction cannot be in the future")
    private LocalDate transactionDate;

    @NotNull
    @Positive(message = "Purchase amount must be positive")
    private BigDecimal purchaseAmount;
}
