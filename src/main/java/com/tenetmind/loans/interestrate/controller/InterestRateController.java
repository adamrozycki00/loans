package com.tenetmind.loans.interestrate.controller;

import com.tenetmind.loans.interestrate.domainmodel.InterestRateDto;
import com.tenetmind.loans.interestrate.domainmodel.InterestRateMapper;
import com.tenetmind.loans.interestrate.service.InterestRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/v1/interest_rates")
public class InterestRateController {

    @Autowired
    private InterestRateService service;

    @Autowired
    private InterestRateMapper mapper;

    @RequestMapping(value = "", method = GET)
    public List<InterestRateDto> getAll() {
        return mapper.mapToDtoList(service.findAll());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public InterestRateDto get(@PathVariable Long id) throws InterestRateNotFoundException {
        return mapper.mapToDto(service.findById(id).orElseThrow(InterestRateNotFoundException::new));
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@PathVariable Long id) throws InterestRateNotFoundException {
        try {
            service.deleteById(id);
        } catch (Exception e) {
            throw new InterestRateNotFoundException();
        }
    }

    @RequestMapping(value = "", method = PUT)
    public InterestRateDto update(@RequestBody InterestRateDto interestRateDto) {
        return mapper.mapToDto(service.save(mapper.mapToExistingEntity(interestRateDto)));
    }

    @RequestMapping(value = "", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody InterestRateDto interestRateDto) {
        service.save(mapper.mapToNewEntity(interestRateDto));
    }

}
