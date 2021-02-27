package com.tenetmind.loans.customer.service;

import com.tenetmind.loans.customer.domainmodel.Customer;
import com.tenetmind.loans.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return repository.findById(id);
    }

    public Customer save(Customer customer) {
        Optional<Customer> retrievedCustomer = find(customer.getPesel());
        return retrievedCustomer.orElseGet(() -> repository.save(customer));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<Customer> find(String pesel) {
        return repository.findByPesel(pesel);
    }
}
