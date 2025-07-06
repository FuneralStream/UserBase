package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDto {
    private Long id;
    private int age;
    private LocalDateTime created_at;
    private String email;
    private String name;
}
