package com.tenetmind.loans.currency.controller;

import com.tenetmind.loans.currency.domainmodel.CurrencyDto;
import com.tenetmind.loans.currency.domainmodel.CurrencyMapper;
import com.tenetmind.loans.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/v1/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService service;

    @Autowired
    private CurrencyMapper mapper;

    @RequestMapping(value = "", method = GET)
    public List<CurrencyDto> getAll() {
        return mapper.mapToDtoList(service.findAll());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public CurrencyDto get(@PathVariable Long id) throws CurrencyNotFoundException {
        return mapper.mapToDto(service.findById(id).orElseThrow(CurrencyNotFoundException::new));
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@PathVariable Long id) throws CurrencyNotFoundException {
        try {
            service.deleteById(id);
        } catch (Exception e) {
            throw new CurrencyNotFoundException();
        }
    }

    @RequestMapping(value = "", method = PUT)
    public CurrencyDto update(@RequestBody CurrencyDto currencyDto) {
        return mapper.mapToDto(service.save(mapper.mapToExistingEntity(currencyDto)));
    }

    @RequestMapping(value = "", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody CurrencyDto currencyDto) {
        service.save(mapper.mapToNewEntity(currencyDto));
    }

}
