package com.tenetmind.loans.customer.controller;

import com.tenetmind.loans.customer.domainmodel.UserDto;
import com.tenetmind.loans.customer.domainmodel.UserMapper;
import com.tenetmind.loans.customer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserMapper mapper;

    @RequestMapping(value = "", method = GET)
    public List<UserDto> getAll() {
        return mapper.mapToDtoList(service.findAll());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public UserDto get(@PathVariable Long id) throws UserNotFoundException {
        return mapper.mapToDto(service.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@PathVariable Long id) throws UserNotFoundException {
        try {
            service.deleteById(id);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    @RequestMapping(value = "", method = PUT)
    public UserDto update(@RequestBody UserDto currencyDto) {
        return mapper.mapToDto(service.save(mapper.mapToEntity(currencyDto)));
    }

    @RequestMapping(value = "", method = POST, consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody UserDto currencyDto) {
        service.save(mapper.mapToEntity(currencyDto));
    }

}
