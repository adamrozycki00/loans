package com.tenetmind.loans.application.controller;

import com.tenetmind.loans.application.domainmodel.LoanApplicationDto;
import com.tenetmind.loans.application.domainmodel.LoanApplicationMapper;
import com.tenetmind.loans.application.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/v1/applications")
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService service;

    @Autowired
    private LoanApplicationMapper mapper;

    @RequestMapping(value = "", method = GET)
    public List<LoanApplicationDto> getAll() {
        return mapper.mapToDtoList(service.findAll());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public LoanApplicationDto get(@PathVariable Long id) throws LoanApplicationNotFoundException {
        return mapper.mapToDto(service.findById(id).orElseThrow(LoanApplicationNotFoundException::new));
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@PathVariable Long id) throws LoanApplicationNotFoundException {
        try {
            service.deleteById(id);
        } catch (Exception e) {
            throw new LoanApplicationNotFoundException();
        }
    }

    @RequestMapping(value = "", method = PUT)
    public LoanApplicationDto update(@RequestBody LoanApplicationDto loanApplicationDto) {
        return mapper.mapToDto(service.save(mapper.mapToExistingEntity(loanApplicationDto)));
    }

    @RequestMapping(value = "", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody LoanApplicationDto loanApplicationDto) {
        service.save(mapper.mapToNewEntity(loanApplicationDto));
    }

}
