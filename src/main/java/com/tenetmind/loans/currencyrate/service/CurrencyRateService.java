package com.tenetmind.loans.currencyrate.service;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyRateService {

    @Autowired
    private CurrencyRateRepository repository;

    public List<CurrencyRate> findAll() {
        return repository.findAll();
    }

    public Optional<CurrencyRate> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<CurrencyRate> getRate(LocalDate date, Currency currency) {
        return repository.findByDateAndCurrency(date, currency);
    }

    public CurrencyRate save(CurrencyRate rate) {
        return repository.save(rate);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
