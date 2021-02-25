package com.tenetmind.loans.currencyrate.service;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClient;
import com.tenetmind.loans.currencyrate.client.nbp.NbpRatesDto;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateNotFoundException;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private CurrencyRateClient rateClient;

    public List<CurrencyRate> findAll() {
        return repository.findAll();
    }

    public Optional<CurrencyRate> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<CurrencyRate> getRate(LocalDate date, String currencyName) throws CurrencyNotFoundException {
        Currency currency = currencyService.find(currencyName)
                .orElseThrow(CurrencyNotFoundException::new);
        return repository.findByDateAndCurrency(date, currency);
    }

    public CurrencyRate save(CurrencyRate rate) throws CurrencyNotFoundException {
        Optional<CurrencyRate> optionalCurrency = getRate(rate.getDate(), rate.getCurrency().getName());
        return optionalCurrency.orElseGet(() -> repository.save(rate));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void populateCurrencyRates() {
        List<Currency> currencies = populateCurrencies();
        LocalDate startingDate = LocalDate.of(2021, 1, 1);
        LocalDate lastDate = LocalDate.now();

        startingDate.datesUntil(lastDate.plusDays(1))
                .forEach(date ->
                        currencies.forEach(currency -> {
                            try {
                                getNewRateAndSave(currency.getName(), date);
                            } catch (CurrencyNotFoundException e) {
                                e.printStackTrace();
                            }
                        })
                );
    }

    private List<Currency> populateCurrencies() {
        List<Currency> currencies = Arrays.asList(
                new Currency("pln"),
                new Currency("eur"),
                new Currency("usd")
        );
        currencies.forEach(currencyService::save);
        return currencies;
    }

    public void getNewRateAndSave(String currencyName, LocalDate date)
            throws CurrencyNotFoundException {
        Optional<CurrencyRate> fromNbp = getFromNbp(currencyName, date);
        if (fromNbp.isPresent()) {
            Optional<CurrencyRate> currencyRate = getRate(date, currencyName);
            if (currencyRate.isEmpty()) {
                save(fromNbp.get());
            }
        }
    }

    private Optional<CurrencyRate> getFromNbp(String currencyName, LocalDate date)
            throws CurrencyNotFoundException {
        Optional<Currency> currency = currencyService.find(currencyName);

        if (currency.isEmpty())
            throw new CurrencyNotFoundException();

        Optional<NbpRatesDto> nbpRate = rateClient.getNbpRates(currencyName, date.toString());

        if (nbpRate.isPresent()) {
            BigDecimal rate = new BigDecimal(nbpRate.get().getRates().get(0).getMid());
            CurrencyRate currencyRate = new CurrencyRate(date, currency.get(), rate);
            return Optional.of(currencyRate);
        } else {
            return Optional.empty();
        }
    }

}
