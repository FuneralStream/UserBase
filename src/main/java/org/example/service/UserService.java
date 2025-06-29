package org.example.service;

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

    void save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void update(User user);
    void delete(User user);
    void handleUserCommand(int command);
}