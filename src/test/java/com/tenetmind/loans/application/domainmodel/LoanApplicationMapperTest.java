package com.tenetmind.loans.application.domainmodel;

import com.tenetmind.loans.application.repository.LoanApplicationRepository;
import com.tenetmind.loans.application.service.LoanApplicationService;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.domainmodel.CurrencyDto;
import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.currency.repository.CurrencyRepository;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.domainmodel.CustomerDto;
import com.tenetmind.loans.customer.domainmodel.CustomerMapper;
import com.tenetmind.loans.customer.repository.CustomerRepository;
import com.tenetmind.loans.loan.repository.LoanRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanApplicationMapperTest {

    @Autowired
    private LoanApplicationMapper mapper;

    @Autowired
    private LoanApplicationService service;

    @Autowired
    private LoanApplicationRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Before
    public void setUp() {
        repository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
        currencyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void shouldReturnNewApplication() {
        //given
        Customer customer = new Customer("John", "Smith", "12345");
        CustomerDto customerDto = new CustomerDto(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getPesel());
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        CurrencyDto plnDto = new CurrencyDto(pln.getId(), pln.getName());
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        repository.save(application);
        LoanApplicationDto applicationDto = new LoanApplicationDto(application.getId(), application.getDate(),
                customerDto, plnDto, application.getAmount(), application.getPeriod(), application.getMarginRate(),
                application.getStatus());
        int applicationsSizeBeforeCreatingAndSavingNewApplication = repository.findAll().size();
        long oldId = application.getId();

        //when
        LoanApplication newApplication = mapper.mapToNewEntity(applicationDto);
        customerRepository.save(newApplication.getCustomer());
        currencyRepository.save(newApplication.getCurrency());
        repository.save(newApplication);
        int applicationsSizeAfterCreatingAndSavingNewApplication = repository.findAll().size();
        long newId = newApplication.getId();

        //then
        assertTrue(applicationsSizeAfterCreatingAndSavingNewApplication > applicationsSizeBeforeCreatingAndSavingNewApplication);
        assertTrue(newId != oldId);
    }

    @Test
    public void shouldReturnExistingApplication() {
        //given
        Customer customer = new Customer("John", "Smith", "12345");
        CustomerDto customerDto = new CustomerDto(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getPesel());
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        CurrencyDto plnDto = new CurrencyDto(pln.getId(), pln.getName());
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        repository.save(application);
        LoanApplicationDto applicationDto = new LoanApplicationDto(application.getId(), application.getDate(),
                customerDto, plnDto, application.getAmount(), application.getPeriod(), application.getMarginRate(),
                application.getStatus());
        int applicationsSizeBeforeCreatingAndSavingNewApplication = repository.findAll().size();
        long oldId = application.getId();

        //when
        LoanApplication newApplication = mapper.mapToExistingEntity(applicationDto);
        customerRepository.save(newApplication.getCustomer());
        currencyRepository.save(newApplication.getCurrency());
        repository.save(newApplication);
        int applicationsSizeAfterCreatingAndSavingNewApplication = repository.findAll().size();
        long newId = newApplication.getId();

        //then
        assertEquals(applicationsSizeAfterCreatingAndSavingNewApplication, applicationsSizeBeforeCreatingAndSavingNewApplication);
        assertEquals(newId, oldId);
    }

    @Test
    public void shouldCreateDtoWithSamePropertiesAsThoseInInputApplication() {
        //given
        Customer customer = new Customer("John", "Smith", "12345");
        CustomerDto customerDto = new CustomerDto(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getPesel());
        customerRepository.save(customer);

        Currency pln = new Currency("PLN");
        CurrencyDto plnDto = new CurrencyDto(pln.getId(), pln.getName());
        currencyRepository.save(pln);

        LoanApplication application = new LoanApplication(LocalDateTime.now(), customer, pln,
                new BigDecimal("1000"), 12, new BigDecimal(".05"));
        repository.save(application);

        LoanApplicationDto applicationDto = new LoanApplicationDto(application.getId(), application.getDate(),
                customerDto, plnDto, application.getAmount(), application.getPeriod(), application.getMarginRate(),
                application.getStatus());

        //when
        LoanApplicationDto newApplicationDto = mapper.mapToDto(application);

        //then
        assertEquals(applicationDto.getId(), newApplicationDto.getId());
        assertEquals(applicationDto.getAmount(), newApplicationDto.getAmount());
        assertEquals(applicationDto.getCurrencyDto().getName(), newApplicationDto.getCurrencyDto().getName());
        assertEquals(applicationDto.getCustomerDto().getLastName(), newApplicationDto.getCustomerDto().getLastName());
    }

}