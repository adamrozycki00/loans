package com.tenetmind.loans.interestrate.service;

import com.tenetmind.loans.interestrate.domainmodel.InterestRate;
import com.tenetmind.loans.interestrate.repository.InterestRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InterestRateService {

    @Autowired
    private InterestRateRepository repository;

    public List<InterestRate> findAll() {
        return repository.findAll();
    }

    public Optional<InterestRate> findById(Long id) {
        return repository.findById(id);
    }

    public InterestRate save(InterestRate rate) {
        return repository.save(rate);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
