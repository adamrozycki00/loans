package com.tenetmind.loans.customer.domainmodel;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User mapToEntity(final UserDto dto) {
        return new User(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName());
    }

    public UserDto mapToDto(final User entity) {
        return new UserDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName());
    }

    public List<UserDto> mapToDtoList(final List<User> currencies) {
        return currencies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
