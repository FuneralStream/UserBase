package org.example.mapper;


import org.example.dto.UserDto;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User fromDto(UserDto userDto);

    List<UserDto> toDtos(List<User> users);

    void updateFromDto(UserDto dto, @MappingTarget User user);
}
