package com.tenetmind.loans.installment.controller;

import com.tenetmind.loans.application.controller.LoanApplicationController;
import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.application.domainmodel.LoanApplicationDto;
import com.tenetmind.loans.currency.controller.CurrencyController;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.domainmodel.CurrencyDto;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateController;
import com.tenetmind.loans.customer.controller.CustomerController;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.domainmodel.CustomerDto;
import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.installment.domainmodel.InstallmentDto;
import com.tenetmind.loans.installment.domainmodel.InstallmentMapper;
import com.tenetmind.loans.installment.service.InstallmentService;
import com.tenetmind.loans.interestrate.controller.InterestRateController;
import com.tenetmind.loans.loan.controller.LoanController;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.domainmodel.LoanDto;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
public class InstallmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstallmentMapper mapper;

    @MockBean
    private InstallmentService service;

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
    private CurrencyController currencyController;

    @MockBean
    private OperationController operationController;

    @Test
    public void shouldReturnInstallments() throws Exception {
        //given
        CustomerDto customerDto = new CustomerDto(1L, "John", "Smith", "12345");
        CurrencyDto plnDto = new CurrencyDto(1L, "PLN");
        CurrencyDto eurDto = new CurrencyDto(1L, "EUR");
        LoanApplicationDto applicationDto = new LoanApplicationDto(1L, LocalDateTime.now(), customerDto, plnDto,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        LoanDto loanDto = new LoanDto(1L, LocalDateTime.now(), applicationDto, customerDto, plnDto,
                new BigDecimal("1000"), 12, new BigDecimal("0.05"), new BigDecimal("0.05"),
                BigDecimal.ZERO, new BigDecimal("1100"), 0, "New",
                new ArrayList<>(), new ArrayList<>());
        InstallmentDto installmentDto = new InstallmentDto(1L, LocalDate.now(), loanDto.getId(), 1, eurDto,
                new BigDecimal("25"), new BigDecimal("1"));

        List<InstallmentDto> installmentDtos = Arrays.asList(installmentDto);
        when(mapper.mapToDtoList(anyList())).thenReturn(installmentDtos);

        //when & then
        mockMvc.perform(get("/v1/installments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldReturnInstallmentWithGivenId() throws Exception {
        //given
        int testId = 7;

        Customer customer = new Customer("John", "Smith", "12345");
        CustomerDto customerDto = new CustomerDto(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getPesel());

        Currency pln = new Currency("PLN");
        CurrencyDto plnDto = new CurrencyDto(pln.getId(), pln.getName());

        LoanApplication application = new LoanApplication(1L, LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        LoanApplicationDto applicationDto = new LoanApplicationDto(application.getId(), application.getDate(),
                customerDto, plnDto, application.getAmount(), application.getPeriod(), application.getMarginRate(),
                application.getStatus());

        Loan loan = new Loan(1L, LocalDateTime.now(), application, new BigDecimal(".05"));

        Installment installment = new Installment((long) testId, LocalDate.now(), loan, 1, pln,
                new BigDecimal("100"), new BigDecimal("4"));
        InstallmentDto installmentDto = new InstallmentDto(installment.getId(), installment.getDate(), loan.getId(), installment.getNumber(),
                plnDto, installment.getPrincipal(), installment.getInterest());

        when(service.findById((long) testId)).thenReturn(Optional.of(installment));
        when(mapper.mapToDto(ArgumentMatchers.eq(installment))).thenReturn(installmentDto);

        //when & then
        mockMvc.perform(get("/v1/installments/" + testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId)));
    }

    @Test
    public void shouldCallMethodDeleteInstallmentWithGivenIdAsParameter() throws Exception {
        //given
        int testId = 8;

        Customer customer = new Customer("John", "Smith", "12345");
        Currency pln = new Currency("PLN");
        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        Loan loan = new Loan(1L, LocalDateTime.now(), application, new BigDecimal(".05"));
        Installment installment = new Installment((long) testId, LocalDate.now(), loan, 1, pln,
                new BigDecimal("100"), new BigDecimal("4"));

        when(service.findById((long) testId)).thenReturn(Optional.of(installment));

        //when & then
        mockMvc.perform(delete("/v1/installments/" + testId))
                .andExpect(status().isOk());

        //then
        verify(service, times(1)).deleteById((long) testId);
    }

}