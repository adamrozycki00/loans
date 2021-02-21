package com.tenetmind.loans.application.repository;

import com.tenetmind.loans.application.domainmodel.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
}
