package com.tenetmind.loans.currencyrate.repository;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    Optional<CurrencyRate> findByNameAndDateAndCurrency(String name, LocalDate date, Currency currency);

    List<CurrencyRate> findByDate(LocalDate date);

    void deleteByDate(LocalDate date);

}
