package com.tenetmind.loans.currencyrate.controller;

import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRateDto;
import com.tenetmind.loans.currencyrate.domainmodel.CurrencyRateMapper;
import com.tenetmind.loans.currencyrate.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/v1/currency_rates")
public class CurrencyRateController {

    @Autowired
    private CurrencyRateService service;

    @Autowired
    private CurrencyRateMapper mapper;

    @RequestMapping(value = "", method = GET)
    public List<CurrencyRateDto> getAll() {
        return mapper.mapToDtoList(service.findAll());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public CurrencyRateDto get(@PathVariable Long id) throws CurrencyRateNotFoundException {
        return mapper.mapToDto(service.findById(id).orElseThrow(CurrencyRateNotFoundException::new));
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@PathVariable Long id) throws CurrencyRateNotFoundException {
        try {
            service.deleteById(id);
        } catch (Exception e) {
            throw new CurrencyRateNotFoundException();
        }
    }

    @RequestMapping(value = "", method = PUT)
    public CurrencyRateDto update(@RequestBody CurrencyRateDto currencyRateDto) {
        return mapper.mapToDto(service.save(mapper.mapToExistingEntity(currencyRateDto)));
    }

    @RequestMapping(value = "", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody CurrencyRateDto currencyRateDto) {
        service.save(mapper.mapToNewEntity(currencyRateDto));
    }

}
