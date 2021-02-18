package com.tenetmind.loans.currency.domainmodel;

import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.interestrate.domainmodel.InterestRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "currencies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private List<CurrencyRate> rates;
    private List<InterestRate> interestRates;

}
