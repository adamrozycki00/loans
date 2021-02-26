package com.tenetmind.loans;

import com.tenetmind.loans.currencyrate.service.CurrencyRateService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class LoansApp {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LoansApp.class, args);

        CurrencyRateService currencyRateService =
                (CurrencyRateService) context.getBean("currencyRateService");

        currencyRateService.populateCurrencyRates(LocalDate.of(2021, 1, 1));
    }

}
