package com.tenetmind.loans.loanapplication.repository;

import com.tenetmind.loans.loanapplication.domainmodel.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
}
