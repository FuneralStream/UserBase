package org.example.mapper;


import org.example.dto.UserDto;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public UserDto toDto(User user);

    public User fromDto(UserDto userDto);

    public List<UserDto> toDtos(List<User> users);

    public void updateFromDto(UserDto dto, @MappingTarget User user);
}
