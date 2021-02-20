package com.tenetmind.loans.customer.domainmodel;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    public Customer mapToEntity(final CustomerDto dto) {
        return new Customer(
                dto.getFirstName(),
                dto.getLastName());
    }

    public CustomerDto mapToDto(final Customer entity) {
        return new CustomerDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName());
    }

    public List<CustomerDto> mapToDtoList(final List<Customer> currencies) {
        return currencies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
