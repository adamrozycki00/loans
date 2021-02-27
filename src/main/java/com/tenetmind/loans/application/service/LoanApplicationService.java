package com.tenetmind.loans.application.service;

import com.tenetmind.loans.application.controller.LoanApplicationNotFoundException;
import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.application.repository.LoanApplicationRepository;
import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.service.CustomerService;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.InvalidLoanStatusException;
import com.tenetmind.loans.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class LoanApplicationService {

    private final String NEW = "New";
    private final String ACCEPTED = "Accepted";
    private final String DENIED = "Denied";
    private final String CANCELED = "Canceled";

    private final List<String> statusList = List.of(NEW, ACCEPTED, DENIED, CANCELED);

    @Autowired
    private LoanApplicationRepository repository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CustomerService customerService;

    public List<LoanApplication> findAll() {
        return repository.findAll();
    }

    public Optional<LoanApplication> findById(Long id) {
        if (id == null) return Optional.empty();
        return repository.findById(id);
    }

    public LoanApplication save(LoanApplication application) throws InvalidApplicationStatusException,
            InvalidLoanStatusException, CurrencyNotFoundException, LoanApplicationNotFoundException {
        if (!validateStatus(application.getStatus())) {
            throw new InvalidApplicationStatusException();
        }

        Currency currency = application.getCurrency();
        Optional<Currency> retrievedCurrency = currencyService.find(currency.getName());
        if (retrievedCurrency.isPresent()) {
            application.setCurrency(retrievedCurrency.get());
        } else {
            throw new CurrencyNotFoundException();
        }

        Customer customer = application.getCustomer();
        Optional<Customer> retrievedCustomer = customerService.find(customer.getPesel());
        if (retrievedCustomer.isPresent()) {
            application.setCustomer(retrievedCustomer.get());
        } else {
            customerService.save(customer);
        }

        LoanApplication savedApplication = repository.save(application);

        if (savedApplication.getStatus().equals(ACCEPTED)) {
            Loan newLoan = new Loan(LocalDateTime.now(), savedApplication, BigDecimal.ZERO);
            loanService.save(newLoan);
        }

        return savedApplication;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private boolean validateStatus(String status) {
        status = status.substring(0, 1).toUpperCase() + status.substring(1);
        return statusList.contains(status);
    }

}
