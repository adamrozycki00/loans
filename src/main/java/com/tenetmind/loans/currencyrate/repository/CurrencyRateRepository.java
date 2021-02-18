package com.tenetmind.loans.currencyrate.repository;

import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
}
