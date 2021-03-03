package com.tenetmind.loans.currency.controller;

import com.tenetmind.loans.application.controller.LoanApplicationController;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.domainmodel.CurrencyDto;
import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateController;
import com.tenetmind.loans.customer.controller.CustomerController;
import com.tenetmind.loans.installment.controller.InstallmentController;
import com.tenetmind.loans.interestrate.controller.InterestRateController;
import com.tenetmind.loans.loan.controller.LoanController;
import com.tenetmind.loans.operation.controller.OperationController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyMapper mapper;

    @MockBean
    private CurrencyService service;

    @MockBean
    private LoanController loanController;

    @MockBean
    private LoanApplicationController loanApplicationController;

    @MockBean
    private CurrencyRateController currencyRateController;

    @MockBean
    private InterestRateController interestRateController;

    @MockBean
    private CustomerController customerController;

    @MockBean
    private InstallmentController installmentController;

    @MockBean
    private OperationController operationController;

    @Test
    public void shouldReturnCurrencies() throws Exception {
        //given
        CurrencyDto plnDto = new CurrencyDto(1L, "PLN");
        CurrencyDto eurDto = new CurrencyDto(2L, "EUR");
        List<CurrencyDto> currencies = Arrays.asList(plnDto, eurDto);
        when(mapper.mapToDtoList(anyList())).thenReturn(currencies);

        //when & then
        mockMvc.perform(get("/v1/currencies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void shouldReturnCurrencyWithGivenId() throws Exception {
        //given
        int testId = 7;

        Currency pln = new Currency((long) testId, "PLN");
        CurrencyDto plnDto = new CurrencyDto(pln.getId(), pln.getName());

        when(service.findById((long) testId)).thenReturn(Optional.of(pln));
        when(mapper.mapToDto(ArgumentMatchers.eq(pln))).thenReturn(plnDto);

        //when & then
        mockMvc.perform(get("/v1/currencies/" + testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId)));
    }

    @Test
    public void shouldCallMethodDeleteCurrencyWithGivenIdAsParameter() throws Exception {
        //given
        int testId = 8;
        Currency pln = new Currency((long) testId, "PLN");
        when(service.findById((long) testId)).thenReturn(Optional.of(pln));

        //when & then
        mockMvc.perform(delete("/v1/currencies/" + testId))
                .andExpect(status().isOk());

        //then
        verify(service, times(1)).deleteById((long) testId);
    }

}