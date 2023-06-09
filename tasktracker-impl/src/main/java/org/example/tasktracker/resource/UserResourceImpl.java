package org.example.tasktracker.resource;

import java.util.List;

import org.example.tasktracker.dto.task.TaskResponseDto;
import org.example.tasktracker.dto.user.UserCreateDto;
import org.example.tasktracker.dto.user.UserResponseDto;
import org.example.tasktracker.mapper.TaskMapper;
import org.example.tasktracker.mapper.UserMapper;
import org.example.tasktracker.service.TaskService;
import org.springframework.web.bind.annotation.RestController;

import org.example.tasktracker.User;
import org.example.tasktracker.task.Task;
import org.example.tasktracker.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserResourceImpl implements UserResource {

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final TaskService taskService;

    @Override
    public List<UserResponseDto> getAll() {
        log.debug("getAll - start");

        List<User> users = userService.getAll();
        List<UserResponseDto> result = userMapper.toUsersResponseDto(users);

        log.debug("getAll - end: result = {}", result);
        return result;
    }

    @Override
    public UserResponseDto getById(long id) {
        log.debug("getById - start: id = {}", id);

        User user = userService.getById(id);
        UserResponseDto result = userMapper.toUserResponseDto(user);

        log.debug("getById - end: result = {}", result);
        return result;
    }

    @Override
    public List<TaskResponseDto> getAllAssignedTasks(long id) {
        log.debug("getAllAssignedTask - start. Tasks owner(user) has id = {}", id);

        List<Task> tasks = taskService.getAllByAssigneeId(id);
        List<TaskResponseDto> result = taskMapper.toTasksResponseDto(tasks);

        log.debug("getAllAssignedTask - end: result = {}", result);
        return result;
    }

    @Override
    public UserResponseDto create(UserCreateDto dto) {
        log.debug("create - start");

        User user = userMapper.toUser(dto);
        User savedUser = userService.create(user);
        UserResponseDto result = userMapper.toUserResponseDto(savedUser);

        log.debug("create - end: result = {}", result);
        return result;
    }

    @Override
    public void addToProject(long userId, long projectId) {
        log.debug("addToProject - start: userId = {}, projectId = {}", userId, projectId);

        userService.addToProject(userId, projectId);

        log.debug("addToProject - end");
    }
}
