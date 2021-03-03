package com.tenetmind.loans.interestrate.controller;

import com.tenetmind.loans.application.controller.LoanApplicationController;
import com.tenetmind.loans.currency.controller.CurrencyController;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.domainmodel.CurrencyDto;
import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateController;
import com.tenetmind.loans.customer.controller.CustomerController;
import com.tenetmind.loans.installment.controller.InstallmentController;
import com.tenetmind.loans.interestrate.domainmodel.InterestRate;
import com.tenetmind.loans.interestrate.domainmodel.InterestRateDto;
import com.tenetmind.loans.interestrate.domainmodel.InterestRateMapper;
import com.tenetmind.loans.interestrate.service.InterestRateService;
import com.tenetmind.loans.loan.controller.LoanController;
import com.tenetmind.loans.operation.controller.OperationController;
import org.apache.tomcat.jni.Local;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
public class InterestRateControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterestRateMapper mapper;

    @MockBean
    private InterestRateService service;

    @MockBean
    private LoanController loanController;

    @MockBean
    private LoanApplicationController loanApplicationController;

    @MockBean
    private CurrencyRateController currencyRateController;

    @MockBean
    private CurrencyController currencyController;

    @MockBean
    private CustomerController customerController;

    @MockBean
    private InstallmentController installmentController;

    @MockBean
    private OperationController operationController;

    @Test
    public void shouldReturnCuInterestRates() throws Exception {
        //given
        CurrencyDto plnDto = new CurrencyDto(1L, "PLN");
        CurrencyDto eurDto = new CurrencyDto(2L, "EUR");

        InterestRateDto rateDto1 = new InterestRateDto(1L, "WIBOR 3M", LocalDate.now(),
                plnDto, new BigDecimal("0.002100"));
        InterestRateDto rateDto2 = new InterestRateDto(2L, "LIBOR 3M", LocalDate.now(),
                eurDto, new BigDecimal("-0.005543"));

        List<InterestRateDto> rateDtos = Arrays.asList(rateDto1, rateDto2);
        when(mapper.mapToDtoList(anyList())).thenReturn(rateDtos);

        //when & then
        mockMvc.perform(get("/v1/interest_rates")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void shouldReturnInterestRateWithGivenId() throws Exception {
        //given
        int testId = 7;

        Currency pln = new Currency(1L, "PLN");
        CurrencyDto plnDto = new CurrencyDto(pln.getId(), pln.getName());

        InterestRate rate = new InterestRate((long) testId, "WIBOR 3M", LocalDate.now(),
                pln, new BigDecimal("0.002100"));
        InterestRateDto rateDto = new InterestRateDto(rate.getId(), rate.getName(), rate.getDate(),
                plnDto, rate.getRate());

        when(service.findById((long) testId)).thenReturn(Optional.of(rate));
        when(mapper.mapToDto(ArgumentMatchers.eq(rate))).thenReturn(rateDto);

        //when & then
        mockMvc.perform(get("/v1/interest_rates/" + testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId)));
    }

    @Test
    public void shouldCallMethodDeleteWithGivenIdAsParameter() throws Exception {
        //given
        int testId = 8;
        Currency pln = new Currency(1L, "PLN");
        InterestRate rate = new InterestRate((long) testId, "WIBOR 3M", LocalDate.now(),
                pln, new BigDecimal("0.002100"));
        when(service.findById((long) testId)).thenReturn(Optional.of(rate));

        //when & then
        mockMvc.perform(delete("/v1/interest_rates/" + testId))
                .andExpect(status().isOk());

        //then
        verify(service, times(1)).deleteById((long) testId);
    }



}