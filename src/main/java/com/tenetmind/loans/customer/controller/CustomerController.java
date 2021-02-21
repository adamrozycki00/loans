package com.tenetmind.loans.customer.controller;

import com.tenetmind.loans.customer.domainmodel.CustomerDto;
import com.tenetmind.loans.customer.domainmodel.CustomerMapper;
import com.tenetmind.loans.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private CustomerMapper mapper;

    @RequestMapping(value = "", method = GET)
    public List<CustomerDto> getAll() {
        return mapper.mapToDtoList(service.findAll());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public CustomerDto get(@PathVariable Long id) throws CustomerNotFoundException {
        return mapper.mapToDto(service.findById(id).orElseThrow(CustomerNotFoundException::new));
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@PathVariable Long id) throws CustomerNotFoundException {
        try {
            service.deleteById(id);
        } catch (Exception e) {
            throw new CustomerNotFoundException();
        }
    }

    @RequestMapping(value = "", method = PUT)
    public CustomerDto update(@RequestBody CustomerDto currencyDto) {
        return mapper.mapToDto(service.save(mapper.mapToExistingEntity(currencyDto)));
    }

    @RequestMapping(value = "", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody CustomerDto currencyDto) {
        service.save(mapper.mapToNewEntity(currencyDto));
    }

}
