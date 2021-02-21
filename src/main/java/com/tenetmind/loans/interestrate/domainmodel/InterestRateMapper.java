package com.tenetmind.loans.interestrate.domainmodel;

import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InterestRateMapper {

    @Autowired
    private CurrencyMapper currencyMapper;

    public InterestRate mapToNewEntity(final InterestRateDto dto) {
        return new InterestRate(
                dto.getName(),
                dto.getDate(),
                currencyMapper.mapToExistingEntity(dto.getCurrencyDto()),
                dto.getRate());
    }

    public InterestRate mapToExistingEntity(final InterestRateDto dto) {
        return new InterestRate(
                dto.getId(),
                dto.getName(),
                dto.getDate(),
                currencyMapper.mapToExistingEntity(dto.getCurrencyDto()),
                dto.getRate());
    }

    public InterestRateDto mapToDto(final InterestRate entity) {
        return new InterestRateDto(
                entity.getId(),
                entity.getName(),
                entity.getDate(),
                currencyMapper.mapToDto(entity.getCurrency()),
                entity.getRate());
    }

    public List<InterestRateDto> mapToDtoList(final List<InterestRate> interestRates) {
        return interestRates.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<InterestRate> mapToEntityList(final List<InterestRateDto> interestRateDtos) {
        return interestRateDtos.stream()
                .map(this::mapToNewEntity)
                .collect(Collectors.toList());
    }

}
