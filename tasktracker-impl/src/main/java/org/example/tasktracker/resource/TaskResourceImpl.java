package org.example.tasktracker.resource;

import java.util.List;

import org.example.tasktracker.dto.task.TaskCreateDto;
import org.example.tasktracker.dto.task.TaskLiteResponseDto;
import org.example.tasktracker.dto.task.TaskResponseDto;
import org.example.tasktracker.dto.task.TaskUpdateDto;
import org.example.tasktracker.service.TaskService;
import org.springframework.web.bind.annotation.RestController;

import org.example.tasktracker.mapper.TaskMapper;
import org.example.tasktracker.task.Task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskResourceImpl implements TaskResource {

    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @Override
    public List<TaskLiteResponseDto> getAll() {
        log.debug("getAll - start");

        List<Task> tasks = taskService.getAll();
        List<TaskLiteResponseDto> result = taskMapper.toTasksLiteResponseDto(tasks);

        log.debug("getAll - end: result = {}", result);
        return result;
    }

    @Override
    public TaskResponseDto getById(long id) {
        log.debug("getById - start: id = {}", id);

        Task task = taskService.getById(id);
        TaskResponseDto result = taskMapper.toTaskResponseDto(task);

        log.debug("getById - end: result = {}", result);
        return result;
    }

    @Override
    public TaskResponseDto create(TaskCreateDto dto) {
        log.debug("create - start: dto = {}", dto);

        Task task = taskMapper.toTask(dto);
        Task savedTask = taskService.create(task, dto.projectId(), dto.creatorId());
        TaskResponseDto result = taskMapper.toTaskResponseDto(savedTask);

        log.debug("create - end: result = {}", result);
        return result;
    }

    @Override
    public TaskResponseDto update(TaskUpdateDto dto, long id) {
        log.debug("update - start: dto = {}", dto);

        Task task = taskMapper.toTask(dto);
        Task savedTask = taskService.update(task, id);
        TaskResponseDto result = taskMapper.toTaskResponseDto(savedTask);

        log.debug("update - end: result = {}", result);
        return result;
    }

    @Override
    public void deleteById(long id) {
        log.debug("deleteById - start: id = {}", id);

        taskService.deleteById(id);

        log.debug("deleteById - end");
    }

    @Override
    public void assignToUser(long taskId, long userId) {
        log.debug("assignToUser - start: taskId = {}, userId = {}", taskId, userId);

        taskService.assignToUser(taskId, userId);

        log.debug("assignToUser - end");
    }
}
