package org.example.tasktracker.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.example.tasktracker.exception.NotFoundException;
import org.example.tasktracker.mapper.TaskMapper;
import org.example.tasktracker.Project;
import org.example.tasktracker.User;
import org.example.tasktracker.task.Task;
import org.example.tasktracker.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final TaskMapper taskMapper;

    @Transactional(readOnly = true)
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task getById(long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Task.class, id));
    }

    @Transactional(readOnly = true)
    public List<Task> getAllByAssigneeId(long id) {
        return taskRepository.findAllByAssigneeId(id);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllByProjectId(long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAllByProjectId(id, pageable).getContent();
    }

    @Transactional
    public Task create(Task task, long projectId, long creatorId) {
        Project project = projectService.getById(projectId);
        User creator = userService.getById(creatorId);
        task.setProject(project);
        task.setCreator(creator);
        return taskRepository.save(task);
    }

    @Transactional
    public Task update(Task newTask, long id) {
        Task task = getById(id);
        taskMapper.updateTask(newTask, task);
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException(Task.class, id);
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    public void assignToUser(long taskId, long userId) {
        User user = userService.getById(userId);
        Task task = getById(taskId);
        task.setAssignee(user);
        // update в бд
        taskRepository.save(task);
    }
}
