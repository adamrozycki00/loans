package com.tenetmind.loans.application.service;

import com.tenetmind.loans.application.domainmodel.LoanApplication;
import com.tenetmind.loans.application.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class LoanApplicationService {

    private final String NEW = "New";
    private final String ACCEPTED = "Accepted";
    private final String DENIED = "Denied";
    private final String CANCELED = "Canceled";

    private final List<String> statusList = List.of(NEW, ACCEPTED, DENIED, CANCELED);

    @Autowired
    private LoanApplicationRepository repository;

    public List<LoanApplication> findAll() {
        return repository.findAll();
    }

    public Optional<LoanApplication> findById(Long id) {
        return repository.findById(id);
    }

    public LoanApplication save(LoanApplication loanApplication) throws InvalidApplicationStatusException {
        if (!validateStatus(loanApplication.getStatus())) {
            throw new InvalidApplicationStatusException();
        }
        return repository.save(loanApplication);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private boolean validateStatus(String status) {
        status = status.substring(0, 1).toUpperCase() + status.substring(1);
        return statusList.contains(status);
    }

}
