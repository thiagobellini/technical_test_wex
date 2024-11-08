package com.example.technical_test_wex.service;

import com.example.technical_test_wex.dto.feign.DataDTO;
import com.example.technical_test_wex.dto.feign.ResponseExchangeDTO;
import com.example.technical_test_wex.exception.CurrencyNotFoundException;
import com.example.technical_test_wex.feign.IExchangeApiClient;
import com.example.technical_test_wex.utils.StringManipulation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ExchangeApiService {

    private final IExchangeApiClient exchangeApiClient;

    public ExchangeApiService(IExchangeApiClient exchangeApiClient) {
        this.exchangeApiClient = exchangeApiClient;
    }

    public DataDTO getExchangeByCurrencyAndDate(String currency, LocalDate date) {
        String fields = "currency,exchange_rate,record_date";

        currency = StringManipulation.capitalizeWords(currency);
        String filter = String.format("currency:in:%s,record_date:gte:%s", currency, date.toString());

        String sort = "-record_date";

        ResponseExchangeDTO responseExchangeDTO = exchangeApiClient.getExchangeByCurrencyAndDate(fields, filter, sort);

        if (responseExchangeDTO.getDataDTOList().isEmpty()) {
            throw new CurrencyNotFoundException();
        }

        return responseExchangeDTO.getDataDTOList().get(0);
    }
}
