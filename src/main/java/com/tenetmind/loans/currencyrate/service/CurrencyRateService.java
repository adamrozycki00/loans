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

    public CurrencyRate save(CurrencyRate rate) {
        return repository.save(rate);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
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
