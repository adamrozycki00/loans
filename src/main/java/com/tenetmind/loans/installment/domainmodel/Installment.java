package com.tenetmind.loans.installment.domainmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Entity
@Table(name = "installments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private Integer number;
    private Currency currency;
    private BigDecimal principal;
    private BigDecimal interest;

}
