package com.example.technical_test_wex.mapper;

import com.example.technical_test_wex.dto.PurchaseExchangeDTO;
import com.example.technical_test_wex.entity.PurchaseEntity;
import com.example.technical_test_wex.dto.PurchaseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IPurchaseMapper {
    PurchaseEntity toEntity(PurchaseDTO purchaseDTO);

    PurchaseDTO toDTO(PurchaseEntity purchaseEntity);

    @Mapping(source = "purchaseAmount", target = "purchaseAmountOriginal")
    PurchaseExchangeDTO toExchangeDTO(PurchaseDTO purchaseDTO);
}
