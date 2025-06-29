package org.example.service;


import org.example.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void update(User user);
    void delete(User user);
}