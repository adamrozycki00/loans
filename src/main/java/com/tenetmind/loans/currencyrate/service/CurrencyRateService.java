package com.tenetmind.loans.currencyrate.service;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClientService;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyRateService {

    @Autowired
    private CurrencyRateRepository repository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRateClientService clientService;

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

    public boolean checkForUpToDateRate(String currencyName) throws CurrencyNotFoundException {

        Optional<Currency> retrievedCurrency = currencyService.find(currencyName);
        if (retrievedCurrency.isEmpty()) {
            throw new CurrencyNotFoundException();
        }

        return getCurrentRate(currencyName, LocalDate.now()).isPresent();
    }

    @Scheduled(cron = "0 15 12 * * Mon-Fri")
    public void addTodaysRates() {
        currencyService.getNamesOfMainCurrencies()
                .forEach(name -> {
                    try {
                        clientService.getAndSave(name);
                    } catch (CurrencyNotFoundException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void prepareDatabase(LocalDate startingDate) {
        currencyService.populateWithMainCurrencies();

        currencyService.getNamesOfMainCurrencies().forEach(currency ->
                startingDate.datesUntil(LocalDate.now())
                        .forEach(date -> {
                            try {
                                clientService.getAndSave(currency, date);
                            } catch (CurrencyNotFoundException e) {
                                e.printStackTrace();
                            }
                        })
        );
    }

    public Optional<CurrencyRate> getCurrentRate(String currencyName, LocalDate date)
            throws CurrencyNotFoundException {
        Optional<CurrencyRate> currentRate = Optional.empty();

        for (int i = 1; i < 5; ++i) {
            currentRate = getRateOnDate(currencyName, date.minusDays(i));
            if (currentRate.isPresent()) break;
        }

        return currentRate;
    }

    private Optional<CurrencyRate> getRateOnDate(String currencyName, LocalDate date)
            throws CurrencyNotFoundException {
        currencyService.find(currencyName).orElseThrow(CurrencyNotFoundException::new);
        return find(clientService.getName(), date, currencyName);
    }

}
