package com.tenetmind.loans.customer.controller;

import com.tenetmind.loans.application.controller.LoanApplicationController;
import com.tenetmind.loans.currency.controller.CurrencyController;
import com.tenetmind.loans.currencyrate.controller.CurrencyRateController;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.domainmodel.CustomerDto;
import com.tenetmind.loans.customer.domainmodel.CustomerMapper;
import com.tenetmind.loans.customer.service.CustomerService;
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
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerMapper mapper;

    @MockBean
    private CustomerService service;

    @MockBean
    private LoanApplicationController loanApplicationController;

    @MockBean
    private LoanController loanController;

    @MockBean
    private CurrencyController currencyController;

    @MockBean
    private CurrencyRateController currencyRateController;

    @MockBean
    private InterestRateController interestRateController;

    @MockBean
    private InstallmentController installmentController;

    @MockBean
    private OperationController operationController;

    @Test
    public void shouldReturnCustomers() throws Exception {
        //given
        CustomerDto customerDto1 = new CustomerDto(1L, "John", "Smith", "12345");
        CustomerDto customerDto2 = new CustomerDto(2L, "Mary", "Jenkins", "654987");
        List<CustomerDto> customerDtos = List.of(customerDto1, customerDto2);

        when(mapper.mapToDtoList(anyList())).thenReturn(customerDtos);

        //when & then
        mockMvc.perform(get("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void shouldReturnCustomerWithGivenId() throws Exception {
        //given
        int testId = 7;

        Customer customer = new Customer((long) testId, "John", "Smith", "12345");
        CustomerDto customerDto = new CustomerDto(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getPesel());

        when(service.findById((long) testId)).thenReturn(Optional.of(customer));
        when(mapper.mapToDto(ArgumentMatchers.eq(customer))).thenReturn(customerDto);

        //when & then
        mockMvc.perform(get("/v1/customers/" + testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testId)));
    }

    @Test
    public void shouldCallMethodDeleteCustomerWithGivenIdAsParameter() throws Exception {
        //given
        int testId = 8;
        Customer customer = new Customer((long) testId, "John", "Smith", "12345");
        when(service.findById((long) testId)).thenReturn(Optional.of(customer));

        //when & then
        mockMvc.perform(delete("/v1/customers/" + testId))
                .andExpect(status().isOk());

        //then
        verify(service, times(1)).deleteById((long) testId);
    }


}