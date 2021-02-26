package com.tenetmind.loans.currencyrate.client.bloomberg;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClient;
import com.tenetmind.loans.currencyrate.client.nbp.NbpRatesDto;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NbpService {

    @Autowired
    private CurrencyRateRepository repository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRateClient rateClient;

    public void getNewNbpRateAndSave(String currencyName, LocalDate date)
            throws CurrencyNotFoundException {
        Optional<CurrencyRate> fromNbp = getFromNbp(currencyName, date);
        if (fromNbp.isPresent()) {
            Currency currency = currencyService.find(fromNbp.get().getName())
                    .orElseThrow(CurrencyNotFoundException::new);
            Optional<CurrencyRate> rateOptional = repository.findByNameAndDateAndCurrency("NBP", date, currency);
            if (rateOptional.isPresent()) {
                CurrencyRate updatedRate = rateOptional.get();
                updatedRate.setRate(fromNbp.get().getRate());
                repository.save(updatedRate);
            } else {
                repository.save(fromNbp.get());
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
            CurrencyRate currencyRate = new CurrencyRate("NBP", date, currency.get(), rate);
            return Optional.of(currencyRate);
        } else {
            return Optional.empty();
        }
    }

}
