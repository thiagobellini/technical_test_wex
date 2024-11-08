package com.example.technical_test_wex.controller;

import com.example.technical_test_wex.dto.PurchaseExchangeDTO;
import com.example.technical_test_wex.dto.PurchaseDTO;
import com.example.technical_test_wex.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<PurchaseDTO> create(@Valid @RequestBody PurchaseDTO purchaseDTO) {
        PurchaseDTO purchaseDTOCreated = purchaseService.create(purchaseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseDTOCreated);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getAll() {
        List<PurchaseDTO> purchaseDTOList = purchaseService.getAll();

        return ResponseEntity.ok(purchaseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getById(@PathVariable UUID id) {
        PurchaseDTO purchaseDTO = purchaseService.getById(id);

        return ResponseEntity.ok(purchaseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseDTO> update(@PathVariable UUID id, @Valid @RequestBody PurchaseDTO purchaseDTO) {
        PurchaseDTO purchaseDTOUpdated = purchaseService.update(id, purchaseDTO);

        return ResponseEntity.ok(purchaseDTOUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        purchaseService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{currency}/exchange")
    public ResponseEntity<List<PurchaseExchangeDTO>> getAllExchange(@PathVariable String currency) {
        List<PurchaseExchangeDTO> purchaseExchangeDTOList = purchaseService.getAllExchange(currency);

        return ResponseEntity.ok(purchaseExchangeDTOList);
    }

    @GetMapping("/{id}/{currency}/exchange")
    public ResponseEntity<PurchaseExchangeDTO> getByIdExchange(@PathVariable UUID id, @PathVariable String currency) {
        PurchaseExchangeDTO purchaseExchangeDTO = purchaseService.getByIdExchange(id, currency);

        return ResponseEntity.ok(purchaseExchangeDTO);
    }
}
