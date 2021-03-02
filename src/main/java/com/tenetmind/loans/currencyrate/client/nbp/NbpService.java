package com.tenetmind.loans.currencyrate.client.nbp;

import com.tenetmind.loans.currencyrate.client.CurrencyRateClient;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NbpService extends CurrencyRateClientService {

    @Autowired
    @Qualifier("nbpClient")
    private CurrencyRateClient client;

    @Override
    public String getName() {
        return  "NBP";
    }

    @Override
    public CurrencyRateClient getClient() {
        return client;
    }

}
