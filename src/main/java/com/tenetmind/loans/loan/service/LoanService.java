package com.tenetmind.loans.loan.service;

import com.tenetmind.loans.application.controller.LoanApplicationNotFoundException;
import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.application.service.InvalidApplicationStatusException;
import com.tenetmind.loans.application.service.LoanApplicationService;
import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.currency.domainmodel.Currency;
import com.tenetmind.loans.currency.service.CurrencyService;
import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.service.CustomerService;
import com.tenetmind.loans.installment.service.InstallmentService;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.repository.LoanRepository;
import com.tenetmind.loans.operation.service.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final String NEW = "New";
    private final String ACTIVE = "Active";
    private final String CLOSED = "Closed";

    private final List<String> statusList = List.of(NEW, ACTIVE, CLOSED);

    @Autowired
    private LoanRepository repository;

    @Autowired
    private InstallmentService installmentService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoanApplicationService applicationService;

    public List<Loan> findAll() {
        return repository.findAll();
    }

    public Optional<Loan> findById(Long id) {
        if (id == null) return Optional.empty();
        return repository.findById(id);
    }

    public Optional<Loan> find(LoanApplication application) {
        return repository.findByApplication(application);
    }

    public Loan save(Loan loan) throws InvalidLoanStatusException, InvalidApplicationStatusException,
            CurrencyNotFoundException, LoanApplicationNotFoundException {
        if (!validateStatus(loan.getStatus())) {
            throw new InvalidLoanStatusException();
        }

        LoanApplication application = loan.getApplication();
        Optional<LoanApplication> retrievedApplication = applicationService.findById(application.getId());
        if (retrievedApplication.isPresent()) {
            loan.setApplication(retrievedApplication.get());
        } else {
            throw new LoanApplicationNotFoundException();
        }

        Customer customer = loan.getCustomer();
        Optional<Customer> retrievedCustomer = customerService.find(customer.getPesel());
        if (retrievedCustomer.isPresent()) {
            loan.setCustomer(retrievedCustomer.get());
        } else {
            customerService.save(customer);
        }

        Currency currency = loan.getCurrency();
        Optional<Currency> retrievedCurrency = currencyService.find(currency.getName());
        if (retrievedCurrency.isPresent()) {
            loan.setCurrency(retrievedCurrency.get());
        } else {
            throw new CurrencyNotFoundException();
        }

        if (loan.getStatus().equals(NEW) && loan.getSchedule().isEmpty()) {
            loan = repository.save(loan);
            installmentService.makeInitialSchedule(loan);
        }

        return repository.save(loan);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private boolean validateStatus(String status) {
        status = status.substring(0, 1).toUpperCase() + status.substring(1);
        return statusList.contains(status);
    }

}
