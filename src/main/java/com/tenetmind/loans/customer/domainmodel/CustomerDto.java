package com.tenetmind.loans.customer.domainmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomerDto {

    private final Long id;
    private final String firstName;
    private final String lastName;

}
