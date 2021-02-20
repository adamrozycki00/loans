package com.tenetmind.loans.customer.domainmodel;

import com.tenetmind.loans.customer.service.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerTest {

    @Autowired
    private CustomerService service;

    @Test
    public void shouldCreateCustomer() {
        //given
        Customer customer = new Customer(1L, "John", "Smith");

        //when
        service.save(customer);
        int customersSize = service.findAll().size();

        //then
        assertEquals(1, customersSize);
    }
  
}
