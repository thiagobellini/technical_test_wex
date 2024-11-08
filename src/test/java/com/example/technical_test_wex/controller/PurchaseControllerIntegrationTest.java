package com.example.technical_test_wex.controller;

import com.example.technical_test_wex.dto.PurchaseDTO;
import com.example.technical_test_wex.dto.PurchaseExchangeDTO;
import com.example.technical_test_wex.repository.IPurchaseRepository;
import com.example.technical_test_wex.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PurchaseControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IPurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseService purchaseService;

    private String url;
    private PurchaseDTO purchaseDTO;

    @BeforeEach
    void init() {
        purchaseRepository.deleteAll();

        url = "http://localhost:" + port + "/purchase";
        purchaseDTO = new PurchaseDTO(null, "Purchase Amazon", LocalDate.now(), new BigDecimal("100.00"));
    }

    @Test
    void shouldCreatePurchaseTest() {
        ResponseEntity<PurchaseDTO> response = restTemplate.postForEntity(url, purchaseDTO, PurchaseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getIdPurchase());
        assertEquals(purchaseDTO.getDescription(), response.getBody().getDescription());
        assertEquals(purchaseDTO.getTransactionDate(), response.getBody().getTransactionDate());
        assertEquals(purchaseDTO.getPurchaseAmount(), response.getBody().getPurchaseAmount());
    }

    @Test
    void shouldGetAllPurchasesTest() {
        PurchaseDTO savedPurchase = purchaseService.create(purchaseDTO);

        ResponseEntity<List<PurchaseDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PurchaseDTO>>() {});

        PurchaseDTO purchaseDTOResponse = response.getBody().get(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals(savedPurchase.getIdPurchase(), purchaseDTOResponse.getIdPurchase());
        assertEquals(savedPurchase.getDescription(), purchaseDTOResponse.getDescription());
        assertEquals(savedPurchase.getTransactionDate(), purchaseDTOResponse.getTransactionDate());
        assertEquals(savedPurchase.getPurchaseAmount(), purchaseDTOResponse.getPurchaseAmount());
    }

    @Test
    void shouldGetPurchaseByIdTest() {
        PurchaseDTO savedPurchase = purchaseService.create(purchaseDTO);

        url += "/" + savedPurchase.getIdPurchase();
        ResponseEntity<PurchaseDTO> response = restTemplate.getForEntity(url, PurchaseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedPurchase.getIdPurchase(), response.getBody().getIdPurchase());
        assertEquals(savedPurchase.getDescription(), response.getBody().getDescription());
        assertEquals(savedPurchase.getTransactionDate(), response.getBody().getTransactionDate());
        assertEquals(savedPurchase.getPurchaseAmount(), response.getBody().getPurchaseAmount());
    }

    @Test
    void shouldUpdatePurchaseTest() {
        PurchaseDTO savedPurchase = purchaseService.create(purchaseDTO);

        savedPurchase.setDescription("Purchase Amazon Updated");
        savedPurchase.setTransactionDate(purchaseDTO.getTransactionDate().minusDays(1L));
        savedPurchase.setPurchaseAmount(new BigDecimal("200.00"));

        url += "/" + savedPurchase.getIdPurchase();
        HttpEntity<PurchaseDTO> requestUpdate = new HttpEntity<>(savedPurchase);
        ResponseEntity<PurchaseDTO> response = restTemplate.exchange(url, HttpMethod.PUT, requestUpdate, PurchaseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedPurchase.getIdPurchase(), response.getBody().getIdPurchase());
        assertEquals(savedPurchase.getDescription(), response.getBody().getDescription());
        assertEquals(savedPurchase.getTransactionDate(), response.getBody().getTransactionDate());
        assertEquals(savedPurchase.getPurchaseAmount(), response.getBody().getPurchaseAmount());
    }

    @Test
    void shouldDeletePurchaseTest() {
        PurchaseDTO savedPurchase = purchaseService.create(purchaseDTO);

        url += "/" + savedPurchase.getIdPurchase();
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(purchaseRepository.existsById(savedPurchase.getIdPurchase()));
    }

    @Test
    void shouldGetAllPurchasesWithExchange() {
        PurchaseDTO savedPurchase = purchaseService.create(purchaseDTO);

        String currency = "euro";
        url += "/" + currency + "/exchange";
        ResponseEntity<List<PurchaseExchangeDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PurchaseExchangeDTO>>() {});

        PurchaseExchangeDTO purchaseExchangeDTOResponse = response.getBody().get(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals(savedPurchase.getIdPurchase(), purchaseExchangeDTOResponse.getIdPurchase());
        assertEquals(savedPurchase.getDescription(), purchaseExchangeDTOResponse.getDescription());
        assertEquals(savedPurchase.getTransactionDate(), purchaseExchangeDTOResponse.getTransactionDate());
        assertEquals(savedPurchase.getPurchaseAmount(), purchaseExchangeDTOResponse.getPurchaseAmountOriginal());
        assertNotNull(purchaseExchangeDTOResponse.getExchangeRate());
        assertNotNull(purchaseExchangeDTOResponse.getPurchaseAmountExchanged());
    }

    @Test
    void shouldGetPurchaseByIdWithExchange() {
        PurchaseDTO savedPurchase = purchaseService.create(purchaseDTO);

        String currency = "euro";
        url += "/" + savedPurchase.getIdPurchase() + "/" + currency + "/exchange";
        ResponseEntity<PurchaseExchangeDTO> response = restTemplate.getForEntity(url, PurchaseExchangeDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedPurchase.getIdPurchase(), response.getBody().getIdPurchase());
        assertEquals(savedPurchase.getDescription(), response.getBody().getDescription());
        assertEquals(savedPurchase.getTransactionDate(), response.getBody().getTransactionDate());
        assertEquals(savedPurchase.getPurchaseAmount(), response.getBody().getPurchaseAmountOriginal());
        assertNotNull(response.getBody().getExchangeRate());
        assertNotNull(response.getBody().getPurchaseAmountExchanged());
    }
}
