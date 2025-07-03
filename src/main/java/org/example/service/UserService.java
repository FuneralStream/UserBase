package org.example.service;

import org.example.dto.UserDto;
import org.example.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    int CREATE_USER = 1;
    int FIND_USER_BY_ID = 2;
    int LIST_ALL_USERS = 3;
    int UPDATE_USER = 4;
    int DELETE_USER = 5;
    int EXIT = 6;

    List<UserDto> findAll();
    UserDto findById(Long id);
    UserDto create(UserDto dto);
    UserDto update(Long id, UserDto dto);
    void delete(Long id);
}