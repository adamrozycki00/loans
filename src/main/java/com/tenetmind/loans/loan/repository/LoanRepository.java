package com.tenetmind.loans.loan.repository;

import com.tenetmind.loans.loan.domainmodel.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
