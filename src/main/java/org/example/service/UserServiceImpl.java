package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll() {
        return userMapper.toDtos(userRepository.findAll());
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден под id: " + id));
    }

    @Override
    public UserDto create(UserDto dto) {
        return userMapper.toDto(userRepository.save(userMapper.fromDto(dto)));
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User modifyingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден под id: " + id));

        userMapper.updateFromDto(dto, modifyingUser);

        User updatedUser = userRepository.save(modifyingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден под id: " + id));
        userRepository.deleteById(id);
    }
}


