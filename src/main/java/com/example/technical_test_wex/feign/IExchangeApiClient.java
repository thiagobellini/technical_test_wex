package com.example.technical_test_wex.feign;

import com.example.technical_test_wex.dto.feign.ResponseExchangeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "exchangeAPIClient", url = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/")
public interface IExchangeApiClient {

    @GetMapping("v1/accounting/od/rates_of_exchange")
    ResponseExchangeDTO getExchangeByCurrencyAndDate(@RequestParam("fields") String currency, @RequestParam("filter") String date, @RequestParam("sort") String sort);
}
