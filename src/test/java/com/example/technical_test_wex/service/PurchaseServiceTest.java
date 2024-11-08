package com.example.technical_test_wex.service;

import com.example.technical_test_wex.dto.PurchaseDTO;
import com.example.technical_test_wex.dto.PurchaseExchangeDTO;
import com.example.technical_test_wex.dto.feign.DataDTO;
import com.example.technical_test_wex.entity.PurchaseEntity;
import com.example.technical_test_wex.exception.PurchaseNotFoundException;
import com.example.technical_test_wex.mapper.IPurchaseMapper;
import com.example.technical_test_wex.repository.IPurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PurchaseServiceTest {

    @Mock
    private IPurchaseRepository purchaseRepository;

    @Mock
    private IPurchaseMapper purchaseMapper;

    @Mock
    private ExchangeApiService exchangeApiService;

    @InjectMocks
    private PurchaseService purchaseService;

    private PurchaseDTO purchaseDTO;
    private PurchaseEntity purchaseEntity;
    private UUID purchaseId;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        purchaseId = UUID.randomUUID();

        purchaseDTO = new PurchaseDTO(purchaseId, "Purchase Amazon", LocalDate.now(), BigDecimal.valueOf(100.00));
        purchaseEntity = new PurchaseEntity(purchaseId, "Purchase Amazon", LocalDate.now(), BigDecimal.valueOf(100.00));
    }

    @Test
    void createShouldCreatePurchaseTest() {
        when(purchaseMapper.toEntity(any(PurchaseDTO.class))).thenReturn(purchaseEntity);
        when(purchaseMapper.toDTO(any(PurchaseEntity.class))).thenReturn(purchaseDTO);
        when(purchaseRepository.save(any(PurchaseEntity.class))).thenReturn(purchaseEntity);

        PurchaseDTO result = purchaseService.create(purchaseDTO);

        assertNotNull(result);
        assertEquals(purchaseDTO.getIdPurchase(), result.getIdPurchase());
        assertEquals(purchaseDTO.getDescription(), result.getDescription());
        assertEquals(purchaseDTO.getTransactionDate(), result.getTransactionDate());
        assertEquals(purchaseDTO.getPurchaseAmount(), result.getPurchaseAmount());
        verify(purchaseRepository, times(1)).save(purchaseEntity);
    }

    @Test
    void getAllShouldReturnAllPurchaseTest() {
        when(purchaseRepository.findAll()).thenReturn(List.of(purchaseEntity));
        when(purchaseMapper.toDTO(any(PurchaseEntity.class))).thenReturn(purchaseDTO);

        List<PurchaseDTO> result = purchaseService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(purchaseDTO.getIdPurchase(), result.get(0).getIdPurchase());
        assertEquals(purchaseDTO.getDescription(), result.get(0).getDescription());
        assertEquals(purchaseDTO.getTransactionDate(), result.get(0).getTransactionDate());
        assertEquals(purchaseDTO.getPurchaseAmount(), result.get(0).getPurchaseAmount());
        verify(purchaseRepository, times(1)).findAll();
    }

    @Test
    void getByIdShouldReturnPurchaseWhenIdExistsTest() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchaseEntity));
        when(purchaseMapper.toDTO(any(PurchaseEntity.class))).thenReturn(purchaseDTO);

        PurchaseDTO result = purchaseService.getById(purchaseId);

        assertNotNull(result);
        assertEquals(purchaseDTO.getIdPurchase(), result.getIdPurchase());
        assertEquals(purchaseDTO.getDescription(), result.getDescription());
        assertEquals(purchaseDTO.getTransactionDate(), result.getTransactionDate());
        assertEquals(purchaseDTO.getPurchaseAmount(), result.getPurchaseAmount());
        verify(purchaseRepository, times(1)).findById(purchaseId);
    }

    @Test
    void getByIdShouldThrowExceptionWhenIdDoesNotExistTest() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        assertThrows(PurchaseNotFoundException.class, () -> purchaseService.getById(purchaseId));
        verify(purchaseRepository, times(1)).findById(purchaseId);
    }

    @Test
    void updateShouldUpdatePurchaseWhenIdExistsTest() {
        PurchaseDTO purchaseDTOToUpdate = new PurchaseDTO(purchaseId,
                "Purchase Amazon Update",
                LocalDate.now().minusMonths(1L),
                BigDecimal.valueOf(222.00));
        PurchaseEntity purchaseEntityUpdated = new PurchaseEntity(purchaseId,
                "Purchase Amazon Update",
                LocalDate.now().minusMonths(1L),
                BigDecimal.valueOf(222.00));

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchaseEntity));
        when(purchaseRepository.save(any(PurchaseEntity.class))).thenReturn(purchaseEntityUpdated);
        when(purchaseMapper.toDTO(any(PurchaseEntity.class))).thenReturn(purchaseDTOToUpdate);

        PurchaseDTO result = purchaseService.update(purchaseId, purchaseDTOToUpdate);

        assertNotNull(result);
        assertEquals(purchaseDTOToUpdate.getIdPurchase(), result.getIdPurchase());
        assertEquals(purchaseDTOToUpdate.getDescription(), result.getDescription());
        assertEquals(purchaseDTOToUpdate.getTransactionDate(), result.getTransactionDate());
        assertEquals(purchaseDTOToUpdate.getPurchaseAmount(), result.getPurchaseAmount());
        verify(purchaseRepository, times(1)).save(purchaseEntity);
    }

    @Test
    void updateShouldThrowExceptionWhenIdDoesNotExistTest() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        assertThrows(PurchaseNotFoundException.class, () -> purchaseService.update(purchaseId, purchaseDTO));
        verify(purchaseRepository, times(1)).findById(purchaseId);
    }

    @Test
    void shouldDeletePurchaseWhenIdExistsTest() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchaseEntity));

        purchaseService.delete(purchaseId);

        verify(purchaseRepository, times(1)).delete(purchaseEntity);
    }

    @Test
    void deleteShouldThrowExceptionWhenIdDoesNotExistTest() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        assertThrows(PurchaseNotFoundException.class, () -> purchaseService.delete(purchaseId));
        verify(purchaseRepository, times(1)).findById(purchaseId);
    }

    @Test
    void getAllExchangeShouldReturnAllExchangesTest() {
        DataDTO dataDTO = new DataDTO();
        dataDTO.setExchangeRate(BigDecimal.valueOf(0.893));

        PurchaseExchangeDTO purchaseExchangeDTO = new PurchaseExchangeDTO(purchaseId,
                "Purchase Amazon",
                LocalDate.now(),
                BigDecimal.valueOf(100.00),
                null,
                null);

        when(purchaseRepository.findAll()).thenReturn(List.of(purchaseEntity));
        when(purchaseMapper.toDTO(any(PurchaseEntity.class))).thenReturn(purchaseDTO);
        when(purchaseMapper.toExchangeDTO(any(PurchaseDTO.class))).thenReturn(purchaseExchangeDTO);
        when(exchangeApiService.getExchangeByCurrencyAndDate(anyString(), any(LocalDate.class))).thenReturn(dataDTO);

        List<PurchaseExchangeDTO> result = purchaseService.getAllExchange("euro");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(purchaseDTO.getIdPurchase(), result.get(0).getIdPurchase());
        assertEquals(purchaseDTO.getDescription(), result.get(0).getDescription());
        assertEquals(purchaseDTO.getTransactionDate(), result.get(0).getTransactionDate());
        assertEquals(purchaseDTO.getPurchaseAmount(), result.get(0).getPurchaseAmountOriginal());
        assertEquals(dataDTO.getExchangeRate(), result.get(0).getExchangeRate());
        assertEquals(new BigDecimal("89.30"), result.get(0).getPurchaseAmountExchanged());
        verify(exchangeApiService, times(1)).getExchangeByCurrencyAndDate(anyString(), any(LocalDate.class));
    }

    @Test
    void getByIdExchangeShouldReturnExchangeWhenIdExistsTest() {
        DataDTO dataDTO = new DataDTO();
        dataDTO.setExchangeRate(BigDecimal.valueOf(0.893));

        PurchaseExchangeDTO purchaseExchangeDTO = new PurchaseExchangeDTO(purchaseId,
                "Purchase Amazon",
                LocalDate.now(),
                BigDecimal.valueOf(100.00),
                null,
                null);

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchaseEntity));
        when(purchaseMapper.toDTO(any(PurchaseEntity.class))).thenReturn(purchaseDTO);
        when(purchaseMapper.toExchangeDTO(any(PurchaseDTO.class))).thenReturn(purchaseExchangeDTO);
        when(exchangeApiService.getExchangeByCurrencyAndDate(anyString(), any(LocalDate.class))).thenReturn(dataDTO);

        PurchaseExchangeDTO result = purchaseService.getByIdExchange(purchaseId, "euro");

        assertNotNull(result);
        assertEquals(purchaseDTO.getIdPurchase(), result.getIdPurchase());
        assertEquals(purchaseDTO.getDescription(), result.getDescription());
        assertEquals(purchaseDTO.getTransactionDate(), result.getTransactionDate());
        assertEquals(purchaseDTO.getPurchaseAmount(), result.getPurchaseAmountOriginal());
        assertEquals(dataDTO.getExchangeRate(), result.getExchangeRate());
        assertEquals(new BigDecimal("89.30"), result.getPurchaseAmountExchanged());
        verify(exchangeApiService, times(1)).getExchangeByCurrencyAndDate(anyString(), any(LocalDate.class));
    }
}