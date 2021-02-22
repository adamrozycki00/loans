package com.tenetmind.loans.operation.controller;

import com.tenetmind.loans.operation.domainmodel.OperationDto;
import com.tenetmind.loans.operation.domainmodel.OperationMapper;
import com.tenetmind.loans.operation.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/v1/operations")
public class OperationController {

    @Autowired
    private OperationService service;

    @Autowired
    private OperationMapper mapper;

    @RequestMapping(value = "", method = GET)
    public List<OperationDto> getAll() {
        return mapper.mapToDtoList(service.findAll());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public OperationDto get(@PathVariable Long id) throws OperationNotFoundException {
        return mapper.mapToDto(service.findById(id).orElseThrow(OperationNotFoundException::new));
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@PathVariable Long id) throws OperationNotFoundException {
        try {
            service.deleteById(id);
        } catch (Exception e) {
            throw new OperationNotFoundException();
        }
    }

    @RequestMapping(value = "", method = PUT)
    public OperationDto update(@RequestBody OperationDto operationDto) {
        return mapper.mapToDto(service.save(mapper.mapToExistingEntity(operationDto)));
    }

    @RequestMapping(value = "", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody OperationDto operationDto) {
        service.save(mapper.mapToNewEntity(operationDto));
    }




}
