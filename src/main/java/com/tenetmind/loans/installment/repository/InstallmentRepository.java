package com.tenetmind.loans.installment.repository;

import com.tenetmind.loans.installment.domainmodel.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {
}
