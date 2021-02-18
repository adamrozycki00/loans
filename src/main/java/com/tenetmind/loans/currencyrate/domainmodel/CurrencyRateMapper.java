package com.tenetmind.loans.currencyrate.domainmodel;

import org.springframework.stereotype.Component;

@Component
public class CurrencyRateMapper {

    public CurrencyRate mapToEntity(final CurrencyRateDto dto) {
        return new CurrencyRate(
                dto.getId(),
                dto.getDate(),
                dto.getCurrency(),
                dto.getRate());
    }

    public CurrencyRateDto mapToDto(final CurrencyRate entity) {
        return new CurrencyRateDto(
                entity.getId(),
                entity.getDate(),
                entity.getCurrency(),
                entity.getRate());
    }

}
