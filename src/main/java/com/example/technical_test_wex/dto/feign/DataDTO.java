package com.example.technical_test_wex.dto.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDTO {

    private String currency;

    @JsonProperty("exchange_rate")
    private BigDecimal exchangeRate;

    @JsonProperty("record_date")
    private LocalDate recordDate;
}
