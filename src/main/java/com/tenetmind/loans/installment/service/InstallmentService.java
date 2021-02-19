package com.tenetmind.loans.installment.service;

import com.tenetmind.loans.installment.domainmodel.Installment;
import com.tenetmind.loans.installment.repository.InstallmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstallmentService {

    @Autowired
    private InstallmentRepository repository;

    public List<Installment> findAll() {
        return repository.findAll();
    }

    public Optional<Installment> findById(Long id) {
        return repository.findById(id);
    }

    public Installment save(Installment installment) {
        return repository.save(installment);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
