package com.example.technical_test_wex.dto.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseExchangeDTO {

    @JsonProperty("data")
    private List<DataDTO> dataDTOList;
}
