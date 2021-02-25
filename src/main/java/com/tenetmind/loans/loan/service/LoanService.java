package com.tenetmind.loans.loan.service;

import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.application.service.InvalidApplicationStatusException;
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

    public List<Loan> findAll() {
        return repository.findAll();
    }

    public Optional<Loan> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Loan> find(LoanApplication application) {
        return repository.findByApplication(application);
    }

    public Loan save(Loan loan) throws InvalidLoanStatusException {
        if (!validateStatus(loan.getStatus())) {
            throw new InvalidLoanStatusException();
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
