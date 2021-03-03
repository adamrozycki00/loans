package com.tenetmind.loans.application.controller;

import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.application.domainmodel.LoanApplicationDto;
import com.tenetmind.loans.application.domainmodel.LoanApplicationMapper;
import com.tenetmind.loans.application.service.LoanApplicationService;
import com.tenetmind.loans.currency.controller.CurrencyController;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.domainmodel.CurrencyDto;
import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateController;
import com.tenetmind.loans.customer.controller.CustomerController;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.domainmodel.CustomerDto;
import com.tenetmind.loans.customer.domainmodel.CustomerMapper;
import com.tenetmind.loans.customer.repository.CustomerRepository;
import com.tenetmind.loans.installment.controller.InstallmentController;
import com.tenetmind.loans.interestrate.controller.InterestRateController;
import com.tenetmind.loans.loan.controller.LoanController;
import com.tenetmind.loans.operation.controller.OperationController;
import com.google.gson.*;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
public class LoanApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanApplicationMapper mapper;

    @MockBean
    private LoanApplicationService service;

    @MockBean
    private LoanController loanController;

    @MockBean
    private CurrencyController currencyController;

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

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private CurrencyMapper currencyMapper;

    @MockBean
    private CustomerMapper customerMapper;

    @Test
    public void shouldReturnLoanApplications() throws Exception {
        //given
        CustomerDto customer = new CustomerDto(1L, "John", "Smith", "12345");
        CurrencyDto pln = new CurrencyDto(1L, "PLN");
        LoanApplicationDto application = new LoanApplicationDto(1L, LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        List<LoanApplicationDto> applications = Arrays.asList(application);
        when(mapper.mapToDtoList(anyList())).thenReturn(applications);

        //when & then
        mockMvc.perform(get("/v1/applications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldReturnApplicationWithGivenId() throws Exception {
        //given
        int testId = 7;

        Customer customer = new Customer("John", "Smith", "12345");
        CustomerDto customerDto = new CustomerDto(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getPesel());

        Currency pln = new Currency("PLN");
        CurrencyDto plnDto = new CurrencyDto(pln.getId(), pln.getName());

        LoanApplication application = new LoanApplication((long) testId, LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        LoanApplicationDto applicationDto = new LoanApplicationDto(application.getId(), application.getDate(),
                customerDto, plnDto, application.getAmount(), application.getPeriod(), application.getMarginRate(),
                application.getStatus());

        when(service.findById((long) testId)).thenReturn(Optional.of(application));
        when(mapper.mapToDto(ArgumentMatchers.eq(application))).thenReturn(applicationDto);

        //when & then
        mockMvc.perform(get("/v1/applications/" + testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId)));
    }

    @Test
    public void shouldCallMethodDeleteApplicationWithGivenIdAsParameter() throws Exception {
        //given
        int testId = 8;

        Customer customer = new Customer("John", "Smith", "12345");
        Currency pln = new Currency("PLN");
        LoanApplication application = new LoanApplication((long) testId, LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");

        when(service.findById((long) testId)).thenReturn(Optional.of(application));

        //when & then
        mockMvc.perform(delete("/v1/applications/" + testId))
                .andExpect(status().isOk());

        //then
        verify(service, times(1)).deleteById((long) testId);
    }

}