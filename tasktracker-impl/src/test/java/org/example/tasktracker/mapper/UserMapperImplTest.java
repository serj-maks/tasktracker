package org.example.tasktracker.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import org.example.tasktracker.dto.user.UserCreateDto;
import org.example.tasktracker.dto.user.UserResponseDto;
import org.example.tasktracker.User;

public class UserMapperImplTest {

    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toUserResponseDto() {
        User user = new User()
            .setId(1L)
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        UserResponseDto dto = userMapper.toUserResponseDto(user);

        assertEquals(user.getId(), dto.id());
        assertEquals(user.getName(), dto.name());
        assertEquals(user.getEmail(), dto.email());
    }

    @Test
    void toUsersResponseDto() {
        User user = new User()
            .setId(1L)
            .setName("Ivan")
            .setEmail("ivan@gmail.com");
        List<UserResponseDto> actualDtos = userMapper.toUsersResponseDto(List.of(user));

        UserResponseDto expectedDto = new UserResponseDto(1L, "Ivan", "ivan@gmail.com");
        List<UserResponseDto> expectedDtos = List.of(expectedDto);

        assertEquals(expectedDtos, actualDtos);
    }

    @Test
    void toUser() {
        UserCreateDto dto = new UserCreateDto("Ivan", "ivan@gmail.com");

        User user = userMapper.toUser(dto);

        assertEquals(user.getName(), dto.name());
        assertEquals(user.getEmail(), dto.email());
    }
}
