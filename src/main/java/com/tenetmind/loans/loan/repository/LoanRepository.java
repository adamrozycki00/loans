package com.tenetmind.loans.loan.repository;

import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.loan.domainmodel.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByApplication(LoanApplication application);

}
