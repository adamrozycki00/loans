package com.tenetmind.loans.installment.controller;

import com.tenetmind.loans.installment.domainmodel.InstallmentDto;
import com.tenetmind.loans.installment.domainmodel.InstallmentMapper;
import com.tenetmind.loans.installment.service.InstallmentService;
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
public class InstallmentController {

    @Autowired
    private InstallmentService service;

    @Autowired
    private InstallmentMapper mapper;

    @RequestMapping(value = "/interest_rates", method = GET)
    public List<InstallmentDto> getAll() {
        return mapper.mapToDtoList(service.findAll());
    }

    @RequestMapping(value = "/interest_rates/{id}", method = GET)
    public InstallmentDto get(@PathVariable Long id) throws InstallmentNotFoundException {
        return mapper.mapToDto(service.findById(id).orElseThrow(InstallmentNotFoundException::new));
    }

    @RequestMapping(value = "/interest_rates/{id}", method = DELETE)
    public void delete(@PathVariable Long id) throws InstallmentNotFoundException {
        try {
            service.deleteById(id);
        } catch (Exception e) {
            throw new InstallmentNotFoundException();
        }
    }

    @RequestMapping(value = "/interest_rates", method = PUT)
    public InstallmentDto update(@RequestBody InstallmentDto installmentDto) {
        return mapper.mapToDto(service.save(mapper.mapToEntity(installmentDto)));
    }

    @RequestMapping(value = "/interest_rates", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody InstallmentDto installmentDto) {
        service.save(mapper.mapToEntity(installmentDto));
    }

}
