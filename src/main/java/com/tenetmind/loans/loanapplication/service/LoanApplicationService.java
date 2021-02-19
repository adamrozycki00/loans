package com.tenetmind.loans.loanapplication.service;

import com.tenetmind.loans.loanapplication.domainmodel.LoanApplication;
import com.tenetmind.loans.loanapplication.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanApplicationService {

    @Autowired
    private LoanApplicationRepository repository;

    public List<LoanApplication> findAll() {
        return repository.findAll();
    }

    public Optional<LoanApplication> findById(Long id) {
        return repository.findById(id);
    }

    public LoanApplication save(LoanApplication loanApplication) {
        return repository.save(loanApplication);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
