package com.tenetmind.loans.currencyrate.client.nbp;

import com.tenetmind.loans.currencyrate.client.CurrencyRateClient;
import com.tenetmind.loans.currencyrate.client.CurrencyRateClientService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NbpService extends CurrencyRateClientService {

    @Override
    protected void setName(String name) {
        this.name = "NBP";
    }

    @Qualifier("nbpClient")
    protected void setClient(CurrencyRateClient client) {
        this.client = client;
    }

}
