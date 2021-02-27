package com.tenetmind.loans;

import com.tenetmind.loans.currencyrate.service.CurrencyRateService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class LoansApp {

    public static void main(String[] args) {
        SpringApplication.run(LoansApp.class, args);

//        ((CurrencyRateService)
//                SpringApplication.run(LoansApp.class, args)
//                .getBean("currencyRateService"))
//                .populateCurrencyRates(LocalDate.of(2021, 1, 1));
    }

}
