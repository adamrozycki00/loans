package com.tenetmind.loans.operation.repository;

import com.tenetmind.loans.operation.domainmodel.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
}
