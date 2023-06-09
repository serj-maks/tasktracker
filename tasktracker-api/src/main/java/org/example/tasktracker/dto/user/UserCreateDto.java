package org.example.tasktracker.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateDto(@NotBlank String name,
                            @Email String email) {

}
