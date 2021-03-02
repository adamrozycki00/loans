package com.tenetmind.loans.currencyrate.service;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.client.bloomberg.BloombergService;
import com.tenetmind.loans.currencyrate.client.nbp.NbpService;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyRateService {

    @Autowired
    private CurrencyRateRepository repository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private NbpService nbpService;

    @Autowired
    private BloombergService bloombergService;

    public List<CurrencyRate> findAll() {
        return repository.findAll();
    }

    public Optional<CurrencyRate> findById(Long id) {
        if (id == null) return Optional.empty();
        return repository.findById(id);
    }

    public Optional<CurrencyRate> find(String name, LocalDate date, String currencyName)
            throws CurrencyNotFoundException {
        Currency currency = currencyService.find(currencyName)
                .orElseThrow(CurrencyNotFoundException::new);
        return repository.findByNameAndDateAndCurrency(name, date, currency);
    }

    public CurrencyRate save(CurrencyRate rate) throws CurrencyNotFoundException {
        Optional<CurrencyRate> optionalCurrency = find(rate.getName(), rate.getDate(), rate.getCurrency().getName());
        return optionalCurrency.orElseGet(() -> repository.save(rate));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void prepareDatabase(LocalDate startingDate) {
        List<Currency> currencies = populateCurrencies();

        currencies.forEach(currency -> {
            String currencyName = currency.getName();

            startingDate.datesUntil(LocalDate.now().plusDays(1))
                    .forEach(date -> {
                        try {
                            nbpService.getAndSave(currencyName, date);
                        } catch (CurrencyNotFoundException e) {
                            e.printStackTrace();
                        }
                    });

            try {
                bloombergService.getAndSave(currencyName);
            } catch (CurrencyNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private List<Currency> populateCurrencies() {
        List<Currency> currencies = Arrays.asList(
                new Currency("pln"),
                new Currency("gbp"),
                new Currency("eur"),
                new Currency("usd")
        );
        currencies.forEach(currencyService::save);
        return currencies;
    }

}
