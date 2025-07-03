package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;


    @Override
    public List<UserDto> findAll() {
        return List.of();
    }

    @Override
    public UserDto findById(Long id) {
        return null;
    }

    @Override
    public UserDto create(UserDto dto) {
        return null;
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}