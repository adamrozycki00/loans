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
        if (id == null) return Optional.empty();
        return repository.findById(id);
    }

    public Optional<Currency> find(String currencyName) {
        return repository.findByName(currencyName.toUpperCase());
    }

    public Currency save(Currency currency) {
        Optional<Currency> optionalCurrency = find(currency.getName());
        return optionalCurrency.orElseGet(() -> repository.save(currency));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<String> getNamesOfMainCurrencies() {
        return List.of("EUR", "USD", "GBP");
    }

    public void populateWithMainCurrencies() {
        getNamesOfMainCurrencies()
                .forEach(name -> {
                    Optional<Currency> currencyRetrievedByName = repository.findByName(name);
                    if (currencyRetrievedByName.isEmpty()) {
                        repository.save(new Currency(name));
                    }
                });
        repository.save(new Currency("PLN"));
    }

}
