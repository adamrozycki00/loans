package com.tenetmind.loans.installment.controller;

import com.tenetmind.loans.currency.controller.CurrencyNotFoundException;
import com.tenetmind.loans.installment.domainmodel.InstallmentDto;
import com.tenetmind.loans.installment.domainmodel.InstallmentMapper;
import com.tenetmind.loans.installment.service.InstallmentService;
import com.tenetmind.loans.loan.controller.LoanNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/v1/installments")
public class InstallmentController {

    @Autowired
    private InstallmentService service;

    @Autowired
    private InstallmentMapper mapper;

    @RequestMapping(value = "", method = GET)
    public List<InstallmentDto> getAll() {
        return mapper.mapToDtoList(service.findAll());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public InstallmentDto get(@PathVariable Long id) throws InstallmentNotFoundException {
        return mapper.mapToDto(service.findById(id).orElseThrow(InstallmentNotFoundException::new));
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@PathVariable Long id) throws InstallmentNotFoundException {
        try {
            service.deleteById(id);
        } catch (Exception e) {
            throw new InstallmentNotFoundException();
        }
    }

    @RequestMapping(value = "", method = PUT)
    public InstallmentDto update(@RequestBody InstallmentDto installmentDto)
            throws CurrencyNotFoundException, LoanNotFoundException {
        return mapper.mapToDto(service.save(mapper.mapToExistingEntity(installmentDto)));
    }

    @RequestMapping(value = "", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody InstallmentDto installmentDto)
            throws CurrencyNotFoundException, LoanNotFoundException {
        service.save(mapper.mapToNewEntity(installmentDto));
    }

}
