package com.tenetmind.loans.currencyrate.client.bloomberg;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClient;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRate;
import com.tenetmind.loans.currencyrate.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BloombergService {

    public static final String NAME = "Bloomberg";

    @Autowired
    private CurrencyRateRepository repository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRateClient rateClient;

    public void getNewBloombergRateAndSave() {
        Optional<List<CurrencyRate>> fromBloomberg = getFromBloomberg();
        fromBloomberg.ifPresent(currencyRates ->
                currencyRates.forEach(rate -> {
                    try {
                        Currency currency = currencyService.find(rate.getCurrency().getName())
                                .orElseThrow(CurrencyNotFoundException::new);
                        Optional<CurrencyRate> retrievedCurrencyRate =
                                repository.findByNameAndDateAndCurrency(NAME, LocalDate.now(), currency);
                        if (retrievedCurrencyRate.isPresent()) {
                            CurrencyRate rateOnUpdate = retrievedCurrencyRate.get();
                            rateOnUpdate.setRate(rate.getRate());
                            repository.save(rateOnUpdate);
                        } else {
                            rate.setCurrency(currency);
                            repository.save(rate);
                        }
                    } catch (CurrencyNotFoundException e) {
                        e.printStackTrace();
                    }
                }));
    }

    private Optional<List<CurrencyRate>> getFromBloomberg() {

        Optional<BloombergRatesDto> bloombergRates = rateClient.getBloombergRates("eur", "usd", "gbp");

        if (bloombergRates.isPresent()) {
            BigDecimal eurPlnRate = new BigDecimal(bloombergRates.get().getResult().getEurPlnRate().getRate());
            BigDecimal usdPlnRate = new BigDecimal(bloombergRates.get().getResult().getUsdPlnRate().getRate());
            BigDecimal gbpPlnRate = new BigDecimal(bloombergRates.get().getResult().getGbpPlnRate().getRate());

            LocalDate today = LocalDate.now();

            CurrencyRate eur = new CurrencyRate(NAME, today, new Currency("eur"), eurPlnRate);
            CurrencyRate usd = new CurrencyRate(NAME, today, new Currency("usd"), usdPlnRate);
            CurrencyRate gbp = new CurrencyRate(NAME, today, new Currency("gbp"), gbpPlnRate);

            return Optional.of(List.of(eur, usd, gbp));
        } else {
            return Optional.empty();
        }
    }

}
