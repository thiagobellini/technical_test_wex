package com.example.technical_test_wex.service;

import com.example.technical_test_wex.controller.PurchaseController;
import com.example.technical_test_wex.dto.PurchaseExchangeDTO;
import com.example.technical_test_wex.dto.feign.DataDTO;
import com.example.technical_test_wex.entity.PurchaseEntity;
import com.example.technical_test_wex.dto.PurchaseDTO;
import com.example.technical_test_wex.exception.PurchaseNotFoundException;
import com.example.technical_test_wex.mapper.IPurchaseMapper;
import com.example.technical_test_wex.repository.IPurchaseRepository;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseService {

    private final IPurchaseRepository purchaseRepository;
    private final IPurchaseMapper purchaseMapper;
    private final ExchangeApiService exchangeApiService;

    public PurchaseService(IPurchaseRepository purchaseRepository, IPurchaseMapper purchaseMapper, ExchangeApiService exchangeApiService) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
        this.exchangeApiService = exchangeApiService;
    }

    public PurchaseDTO create(PurchaseDTO purchaseDTO) {
        purchaseDTO.setPurchaseAmount(purchaseDTO.getPurchaseAmount().setScale(2, RoundingMode.HALF_UP));
        PurchaseEntity purchaseEntity = purchaseMapper.toEntity(purchaseDTO);
        PurchaseDTO purchaseDTOCreated = purchaseMapper.toDTO(purchaseRepository.save(purchaseEntity));
        addLinks(purchaseDTOCreated);

        return purchaseDTOCreated;
    }

    public List<PurchaseDTO> getAll() {
        List<PurchaseDTO> purchaseDTOList = purchaseRepository.findAll().stream()
                .map(purchaseMapper::toDTO)
                .toList();
        purchaseDTOList.forEach(this::addLinks);

        return purchaseDTOList;
    }

    public PurchaseDTO getById(UUID id) {
        PurchaseDTO purchaseDTO = purchaseRepository.findById(id)
                .map(purchaseMapper::toDTO)
                .orElseThrow(() -> new PurchaseNotFoundException(id.toString()));
        addLinks(purchaseDTO);

        return purchaseDTO;
    }

    public PurchaseDTO update(UUID id, PurchaseDTO purchaseDTO) {
        PurchaseDTO purchaseDTOUpdated = purchaseRepository.findById(id)
                .map(purchaseEntity -> {
                    purchaseEntity.setDescription(purchaseDTO.getDescription());
                    purchaseEntity.setTransactionDate(purchaseDTO.getTransactionDate());
                    purchaseEntity.setPurchaseAmount(purchaseDTO.getPurchaseAmount().setScale(2, RoundingMode.HALF_UP));

                    return purchaseMapper.toDTO(purchaseRepository.save(purchaseEntity));
                })
                .orElseThrow(() -> new PurchaseNotFoundException(id.toString()));
        addLinks(purchaseDTOUpdated);

        return purchaseDTOUpdated;
    }

    public void delete(UUID id) {
        PurchaseEntity purchaseEntity = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id.toString()));

        purchaseRepository.delete(purchaseEntity);
    }

    public List<PurchaseExchangeDTO> getAllExchange(String currency) {
        List<PurchaseDTO> purchaseDTOList = getAll();

        List<PurchaseExchangeDTO> purchaseExchangeDTOList = purchaseDTOList.stream()
                .map(purchaseDTO -> generatePurchaseExchangeDTO(purchaseDTO, currency))
                .toList();
        purchaseExchangeDTOList.forEach(purchaseExchangeDTO -> addLinks(purchaseExchangeDTO, currency));

        return purchaseExchangeDTOList;
    }

    public PurchaseExchangeDTO getByIdExchange(UUID id, String currency) {
        PurchaseDTO purchaseDTO = getById(id);
        PurchaseExchangeDTO purchaseExchangeDTO = generatePurchaseExchangeDTO(purchaseDTO, currency);
        addLinks(purchaseExchangeDTO, currency);

        return purchaseExchangeDTO;
    }

    private PurchaseExchangeDTO generatePurchaseExchangeDTO(PurchaseDTO purchaseDTO, String currency) {
        PurchaseExchangeDTO purchaseExchangeDTO = purchaseMapper.toExchangeDTO(purchaseDTO);
        LocalDate dateToExchange = calculateDateMinus6Months(purchaseExchangeDTO.getTransactionDate());

        DataDTO dataDTO = exchangeApiService.getExchangeByCurrencyAndDate(currency, dateToExchange);

        purchaseExchangeDTO.setExchangeRate(dataDTO.getExchangeRate());
        purchaseExchangeDTO.setPurchaseAmountExchanged(calculatePurchaseAmountExchanged(purchaseExchangeDTO.getPurchaseAmountOriginal(), dataDTO.getExchangeRate()));

        return purchaseExchangeDTO;
    }

    private LocalDate calculateDateMinus6Months(LocalDate purchaseDate) {
        return purchaseDate.minusMonths(6L);
    }

    private BigDecimal calculatePurchaseAmountExchanged(BigDecimal amountOriginal, BigDecimal exchangeRate) {
        return amountOriginal.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
    }

    private void addLinks(PurchaseDTO purchaseDTO) {
        Link getByIdLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PurchaseController.class).getById(purchaseDTO.getIdPurchase())).withRel("Get by Id").withType("GET");
        Link getAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PurchaseController.class).getAll()).withRel("Get all").withType("GET");
        Link updateLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PurchaseController.class).update(purchaseDTO.getIdPurchase(), purchaseDTO)).withRel("Update").withType("PUT");
        Link deleteLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PurchaseController.class).delete(purchaseDTO.getIdPurchase())).withRel("Delete").withType("DELETE");
        Link getByIdExchangeLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PurchaseController.class).getByIdExchange(purchaseDTO.getIdPurchase(),"currency")).withRel("Get by Id Exchange").withType("GET");
        Link getAllExchangeLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PurchaseController.class).getAllExchange("currency")).withRel("Get all Exchange").withType("GET");

        purchaseDTO.add(getByIdLink, getAllLink, updateLink, deleteLink, getByIdExchangeLink, getAllExchangeLink);
    }

    private void addLinks(PurchaseExchangeDTO purchaseExchangeDTO, String currency) {
        Link getByIdExchangeLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PurchaseController.class).getByIdExchange(purchaseExchangeDTO.getIdPurchase(),currency)).withRel("Get by Id Exchange").withType("GET");
        Link getAllExchangeLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PurchaseController.class).getAllExchange(currency)).withRel("Get all Exchange").withType("GET");
        Link getByIdLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PurchaseController.class).getById(purchaseExchangeDTO.getIdPurchase())).withRel("Get by Id").withType("GET");
        Link getAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PurchaseController.class).getAll()).withRel("Get all").withType("GET");

        purchaseExchangeDTO.add(getByIdExchangeLink, getAllExchangeLink, getByIdLink, getAllLink);
    }
}
