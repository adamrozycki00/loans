package com.tenetmind.loans.currency.service;

import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository repository;

    public List<Currency> findAll() {
        return repository.findAll();
    }

    public Optional<Currency> findById(Long id) {
        return repository.findById(id);
    }

    public Currency save(Currency currency) {
        return repository.save(currency);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
