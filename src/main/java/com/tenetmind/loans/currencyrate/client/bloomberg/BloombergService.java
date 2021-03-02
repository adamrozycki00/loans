package com.tenetmind.loans.currencyrate.client.bloomberg;

import com.tenetmind.loans.currencyrate.client.CurrencyRateClient;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BloombergService extends CurrencyRateClientService {

    @Autowired
    @Qualifier("bloombergClient")
    private CurrencyRateClient client;

    @Override
    public String getName() {
        return  "Bloomberg";
    }

    @Override
    public CurrencyRateClient getClient() {
        return client;
    }

}
