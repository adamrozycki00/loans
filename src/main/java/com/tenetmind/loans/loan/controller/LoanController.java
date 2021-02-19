package com.tenetmind.loans.loan.controller;

import com.tenetmind.loans.loan.domainmodel.LoanDto;
import com.tenetmind.loans.loan.domainmodel.LoanMapper;
import com.tenetmind.loans.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/v1")
public class LoanController {

    @Autowired
    private LoanService service;

    @Autowired
    private LoanMapper mapper;

    @RequestMapping(value = "/operations", method = GET)
    public List<LoanDto> getAll() {
        return mapper.mapToDtoList(service.findAll());
    }

    @RequestMapping(value = "/operations/{id}", method = GET)
    public LoanDto get(@PathVariable Long id) throws LoanNotFoundException {
        return mapper.mapToDto(service.findById(id).orElseThrow(LoanNotFoundException::new));
    }

    @RequestMapping(value = "/operations/{id}", method = DELETE)
    public void delete(@PathVariable Long id) throws LoanNotFoundException {
        try {
            service.deleteById(id);
        } catch (Exception e) {
            throw new LoanNotFoundException();
        }
    }

    @RequestMapping(value = "/operations", method = PUT)
    public LoanDto update(@RequestBody LoanDto loanDto) {
        return mapper.mapToDto(service.save(mapper.mapToEntity(loanDto)));
    }

    @RequestMapping(value = "/operations", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody LoanDto loanDto) {
        service.save(mapper.mapToEntity(loanDto));
    }

}
