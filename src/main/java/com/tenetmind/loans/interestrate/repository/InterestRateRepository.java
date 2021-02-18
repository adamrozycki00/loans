package com.tenetmind.loans.interestrate.repository;

import com.tenetmind.loans.interestrate.domainmodel.InterestRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRateRepository extends JpaRepository<InterestRate, Long> {
}
