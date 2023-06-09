package org.example.tasktracker.resource;

import java.util.List;

import org.example.tasktracker.dto.project.ProjectCreateDto;
import org.example.tasktracker.dto.project.ProjectResponseDto;
import org.example.tasktracker.dto.task.TaskResponseDto;
import org.example.tasktracker.dto.user.UserResponseDto;
import org.example.tasktracker.service.TaskService;
import org.springframework.web.bind.annotation.RestController;

import org.example.tasktracker.mapper.ProjectMapper;
import org.example.tasktracker.mapper.TaskMapper;
import org.example.tasktracker.mapper.UserMapper;
import org.example.tasktracker.Project;
import org.example.tasktracker.User;
import org.example.tasktracker.task.Task;
import org.example.tasktracker.service.ProjectService;
import org.example.tasktracker.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectResourceImpl implements ProjectResource {

    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;

    @Override
    public List<ProjectResponseDto> getAll() {
        log.debug("getAll - start");

        List<Project> projects = projectService.getAll();
        List<ProjectResponseDto> result = projectMapper.toProjectsResponseDto(projects);

        log.debug("getAll - end: result = {}", result);
        return result;
    }

    @Override
    public ProjectResponseDto getById(long id) {
        log.debug("getById - start: id = {}", id);

        Project project = projectService.getById(id);
        ProjectResponseDto result = projectMapper.toProjectResponseDto(project);

        log.debug("getById - end: result = {}", result);
        return result;
    }

    @Override
    public List<UserResponseDto> getAllUsers(long id) {
        log.debug("getUsersFromProject - start");

        List<User> users = userService.getByProjectId(id);
        List<UserResponseDto> result = userMapper.toUsersResponseDto(users);

        log.debug("getUsersFromProject - end: result = {}", result);
        return result;
    }

    @Override
    public List<TaskResponseDto> getAllTasks(long id, int page, int size) {
        log.debug("getAllTasks - start");

        List<Task> tasks = taskService.getAllByProjectId(id, page, size);
        List<TaskResponseDto> result = taskMapper.toTasksResponseDto(tasks);

        log.debug("getAllTasks - end: result = {}", result);
        return result;
    }

    @Override
    public ProjectResponseDto create(ProjectCreateDto dto) {
        log.debug("create - start");

        Project project = projectMapper.toProject(dto);
        Project newProject = projectService.create(project);
        ProjectResponseDto result = projectMapper.toProjectResponseDto(newProject);

        log.debug("create - end: result = {}", result);
        return result;
    }
}
