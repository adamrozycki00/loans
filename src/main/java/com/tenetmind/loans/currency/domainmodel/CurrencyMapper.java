package com.tenetmind.loans.currency.domainmodel;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrencyMapper {

    public Currency mapToNewEntity(final CurrencyDto dto) {
        return new Currency(
                dto.getName());
    }

    public Currency mapToExistingEntity(final CurrencyDto dto) {
        return new Currency(
                dto.getId(),
                dto.getName());
    }

    public CurrencyDto mapToDto(final Currency entity) {
        return new CurrencyDto(
                entity.getId(),
                entity.getName());
    }

    public List<CurrencyDto> mapToDtoList(final List<Currency> currencies) {
        return currencies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
