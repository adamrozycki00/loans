package com.tenetmind.loans.currencyrate.client.bloomberg;

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BloombergService extends CurrencyRateClientService {

    public static final String NAME = "Bloomberg";

    @Autowired
    private CurrencyRateRepository repository;

    @Autowired
    private CurrencyService currencyService;

    @Qualifier("bloombergClient")
    protected void setClient(CurrencyRateClient client) {
        this.client = client;
    }

    @Override
    public void getAndSave(String currencyName, LocalDate date) throws CurrencyNotFoundException {

    }

//    public void getNewBloombergRateAndSaveOrUpdate() {
//        Optional<List<CurrencyRate>> fromBloomberg = client.getFromBloomberg();
//        fromBloomberg.ifPresent(currencyRates ->
//                currencyRates.forEach(rate -> {
//                    try {
//                        Currency currency = currencyService.find(rate.getCurrency().getName())
//                                .orElseThrow(CurrencyNotFoundException::new);
//                        Optional<CurrencyRate> retrievedCurrencyRate =
//                                repository.findByNameAndDateAndCurrency(NAME, LocalDate.now(), currency);
//                        if (retrievedCurrencyRate.isPresent()) {
//                            CurrencyRate rateOnUpdate = retrievedCurrencyRate.get();
//                            rateOnUpdate.setRate(rate.getRate());
//                            repository.save(rateOnUpdate);
//                        } else {
//                            rate.setCurrency(currency);
//                            repository.save(rate);
//                        }
//                    } catch (CurrencyNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }));
//    }

}
