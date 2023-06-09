package org.example.tasktracker.mapper;

import java.util.List;

import org.example.tasktracker.User;
import org.example.tasktracker.dto.user.UserCreateDto;
import org.example.tasktracker.dto.user.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserResponseDto toUserResponseDto(User user);

    List<UserResponseDto> toUsersResponseDto(List<User> users);

    User toUser(UserCreateDto dto);
}
