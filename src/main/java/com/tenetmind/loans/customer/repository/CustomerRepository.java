package com.tenetmind.loans.customer.repository;

import com.tenetmind.loans.customer.domainmodel.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPesel(String pesel);

}
