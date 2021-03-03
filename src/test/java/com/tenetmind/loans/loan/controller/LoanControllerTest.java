package com.tenetmind.loans.loan.controller;

import com.tenetmind.loans.application.controller.LoanApplicationController;
import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.application.domainmodel.LoanApplicationDto;
import com.tenetmind.loans.application.domainmodel.LoanApplicationMapper;
import com.tenetmind.loans.application.repository.LoanApplicationRepository;
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
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.domainmodel.LoanDto;
import com.tenetmind.loans.loan.domainmodel.LoanMapper;
import com.tenetmind.loans.loan.service.LoanService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanMapper mapper;

    @MockBean
    private LoanService service;

    @MockBean
    private LoanApplicationController loanApplicationController;

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
    private LoanApplicationRepository loanApplicationRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private LoanApplicationMapper loanApplicationMapper;

    @MockBean
    private CurrencyMapper currencyMapper;

    @MockBean
    private CustomerMapper customerMapper;

    @Test
    public void shouldReturnLoans() throws Exception {
        //given
        CustomerDto customerDto = new CustomerDto(1L, "John", "Smith", "12345");
        CurrencyDto plnDto = new CurrencyDto(1L, "PLN");
        LoanApplicationDto applicationDto = new LoanApplicationDto(1L, LocalDateTime.now(), customerDto, plnDto,
                new BigDecimal("1000"), 12, new BigDecimal(".05"), "New");
        LoanDto loanDto = new LoanDto(1L, LocalDateTime.now(), applicationDto, customerDto, plnDto,
                new BigDecimal("1000"), 12, new BigDecimal("0.05"), new BigDecimal("0.05"),
                BigDecimal.ZERO, new BigDecimal("1100"), 0, "New",
                new ArrayList<>(), new ArrayList<>());
        List<LoanDto> loans = Arrays.asList(loanDto);
        when(mapper.mapToDtoList(anyList())).thenReturn(loans);

        //when & then
        mockMvc.perform(get("/v1/loans")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldReturnLoanWithGivenId() throws Exception {
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

        Loan loan = new Loan((long) testId, LocalDateTime.now(), application, new BigDecimal(".05"));
        LoanDto loanDto = new LoanDto(loan.getId(), loan.getDate(), applicationDto, customerDto, plnDto,
                loan.getAmount(), loan.getPeriod(), loan.getBaseRate(), loan.getMarginRate(),
                loan.getBalance(), loan.getAmountToPay(), loan.getNumberOfInstallmentsPaid(),
                loan.getStatus(), new ArrayList<>(), new ArrayList<>());

        when(service.findById((long) testId)).thenReturn(Optional.of(loan));
        when(mapper.mapToDto(ArgumentMatchers.eq(loan))).thenReturn(loanDto);

        //when & then
        mockMvc.perform(get("/v1/loans/" + testId)
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
        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        Loan loan = new Loan((long) testId, LocalDateTime.now(), application, new BigDecimal(".05"));

        when(service.findById((long) testId)).thenReturn(Optional.of(loan));

        //when & then
        mockMvc.perform(delete("/v1/loans/" + testId))
                .andExpect(status().isOk());

        //then
        verify(service, times(1)).deleteById((long) testId);
    }


}
