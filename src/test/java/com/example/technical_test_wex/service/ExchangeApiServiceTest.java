package com.example.technical_test_wex.service;

import com.example.technical_test_wex.dto.feign.DataDTO;
import com.example.technical_test_wex.dto.feign.ResponseExchangeDTO;
import com.example.technical_test_wex.exception.CurrencyNotFoundException;
import com.example.technical_test_wex.feign.IExchangeApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ExchangeApiServiceTest {

    @Mock
    private IExchangeApiClient exchangeApiClient;

    @InjectMocks
    private ExchangeApiService exchangeApiService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExchangeByCurrencyAndDateShouldReturnDataDTOWhenCurrencyExistsTest() {
        String currency = "euro";
        LocalDate date = LocalDate.now().minusDays(1);

        DataDTO dataDTO = new DataDTO();
        dataDTO.setCurrency("Euro");
        dataDTO.setExchangeRate(BigDecimal.valueOf(0.893));
        dataDTO.setRecordDate(date);

        ResponseExchangeDTO responseExchangeDTO = new ResponseExchangeDTO();
        responseExchangeDTO.setDataDTOList(List.of(dataDTO));

        when(exchangeApiClient.getExchangeByCurrencyAndDate(anyString(), anyString(), anyString()))
                .thenReturn(responseExchangeDTO);

        DataDTO result = exchangeApiService.getExchangeByCurrencyAndDate(currency, date);

        assertNotNull(result);
        assertEquals("Euro", result.getCurrency());
        assertEquals(BigDecimal.valueOf(0.893), result.getExchangeRate());
        verify(exchangeApiClient, times(1)).getExchangeByCurrencyAndDate(anyString(), anyString(), anyString());
    }

    @Test
    void getExchangeByCurrencyAndDateShouldThrowCurrencyNotFoundExceptionWhenCurrencyDoesNotExistTest() {

        String currency = "invalid";
        LocalDate date = LocalDate.now().minusDays(1);

        ResponseExchangeDTO responseExchangeDTO = new ResponseExchangeDTO();
        responseExchangeDTO.setDataDTOList(Collections.emptyList());

        when(exchangeApiClient.getExchangeByCurrencyAndDate(anyString(), anyString(), anyString()))
                .thenReturn(responseExchangeDTO);

        assertThrows(CurrencyNotFoundException.class, () -> exchangeApiService.getExchangeByCurrencyAndDate(currency, date));
        verify(exchangeApiClient, times(1)).getExchangeByCurrencyAndDate(anyString(), anyString(), anyString());
    }
}