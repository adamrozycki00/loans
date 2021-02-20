package com.tenetmind.loans.customer.repository;

import com.tenetmind.loans.customer.domainmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
