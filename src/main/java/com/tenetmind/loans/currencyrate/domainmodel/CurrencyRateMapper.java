package com.tenetmind.loans.currencyrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrencyRateMapper {

    @Autowired
    private CurrencyMapper currencyMapper;

    public CurrencyRate mapToNewEntity(final CurrencyRateDto dto) {
        return new CurrencyRate(
                dto.getName(),
                dto.getDate(),
                currencyMapper.mapToExistingEntity(dto.getCurrencyDto()),
                dto.getRate());
    }

    public CurrencyRate mapToExistingEntity(final CurrencyRateDto dto) {
        return new CurrencyRate(
                dto.getId(),
                dto.getName(),
                dto.getDate(),
                currencyMapper.mapToExistingEntity(dto.getCurrencyDto()),
                dto.getRate());
    }

    public CurrencyRateDto mapToDto(final CurrencyRate entity) {
        return new CurrencyRateDto(
                entity.getId(),
                entity.getName(),
                entity.getDate(),
                currencyMapper.mapToDto(entity.getCurrency()),
                entity.getRate());
    }

    public List<CurrencyRateDto> mapToDtoList(final List<CurrencyRate> rates) {
        return rates.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CurrencyRate> mapToEntityList(final List<CurrencyRateDto> rateDtos) {
        return rateDtos.stream()
                .map(this::mapToNewEntity)
                .collect(Collectors.toList());
    }

}
