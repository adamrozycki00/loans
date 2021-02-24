package com.tenetmind.loans.installment.service;

import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.installment.repository.InstallmentRepository;
import com.tenetmind.loans.installment.service.interestcalc.InterestCalc;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import com.tenetmind.loans.loan.domainmodel.Loan;
import com.tenetmind.loans.loan.service.LoanService;
import com.tenetmind.loans.operation.service.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InstallmentService {

    @Autowired
    private InstallmentRepository repository;

    public List<Installment> findAll() {
        return repository.findAll();
    }

    public Optional<Installment> findById(Long id) {
        return repository.findById(id);
    }

    public Installment save(Installment installment) {
        return repository.save(installment);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void makeInitialSchedule(Loan loan, InterestCalc interestCalc) {
        int loanPeriod = loan.getPeriod();
        BigDecimal loanBalance = loan.getAmount();
        BigDecimal interestRate = loan.getBaseRate().add(loan.getMarginRate());

        for (int numberOfInstallmentsLeft = loanPeriod; numberOfInstallmentsLeft > 0; --numberOfInstallmentsLeft) {
            BigDecimal principal = interestCalc.calculatePrincipal(numberOfInstallmentsLeft, loanBalance,
                    interestRate);
            BigDecimal interest = interestCalc.calculateInterest(numberOfInstallmentsLeft, loanBalance,
                    interestRate);
            Installment installment = new Installment(
                    loan.getDate().plusMonths(loanPeriod - numberOfInstallmentsLeft + 1L).toLocalDate(),
                    loan, loanPeriod - numberOfInstallmentsLeft + 1, principal, interest);
            save(installment);
            loanBalance = loanBalance.subtract(principal);
        }
    }


}
