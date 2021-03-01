package com.tenetmind.loans.currencyrate.client.nbp;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClient;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClientService;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class NbpService extends CurrencyRateClientService {

    public static final String NAME = "NBP";

    @Autowired
    private CurrencyRateRepository repository;

    @Autowired
    private CurrencyService currencyService;

    @Qualifier("nbpClient")
    protected void setClient(CurrencyRateClient client) {
        this.client = client;
    }

    @Override
    public void getAndSave(String currencyName, LocalDate date) throws CurrencyNotFoundException {
        Optional<CurrencyRate> fromNbp = client.getCurrencyRate(currencyName, date);
        if (fromNbp.isPresent()) {
            Currency currency = currencyService.find(fromNbp.get().getCurrency().getName())
                    .orElseThrow(CurrencyNotFoundException::new);
            Optional<CurrencyRate> retrievedRate = repository.findByNameAndDateAndCurrency(NAME, date, currency);
            if (retrievedRate.isEmpty()) {
                fromNbp.get().setCurrency(currency);
                repository.save(fromNbp.get());
            }
        }
    }

}
