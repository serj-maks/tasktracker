package org.example.tasktracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import org.example.tasktracker.exception.NotFoundException;
import org.example.tasktracker.mapper.TaskMapper;
import org.example.tasktracker.User;
import org.example.tasktracker.task.Task;
import org.example.tasktracker.task.enums.Priority;
import org.example.tasktracker.task.enums.Status;
import org.example.tasktracker.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepositoryMock;

    @InjectMocks
    TaskService taskService;

    @Mock
    UserService userService;

    @Spy
    TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @BeforeEach
    void before() {
        ReflectionTestUtils.setField(taskService, "taskMapper", taskMapper);
    }

    @Test
    void getAll() {
        Long id = 1L;
        List<Task> expected = List.of(new Task()
            .setId(id)
            .setTitle("task title"));

        when(taskRepositoryMock.findAll())
            .thenReturn(expected);
        List<Task> actual = taskService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_returnTask() {
        long id = 1L;
        Task expected = new Task()
            .setId(id)
            .setTitle("task title");

        when(taskRepositoryMock.findById(id))
            .thenReturn(Optional.of(expected));
        Task actual = taskService.getById(id);

        assertEquals(expected, actual);
        verify(taskRepositoryMock).findById(id);
    }

    @Test
    void getById_throwException() {
        assertThrows(NotFoundException.class, () -> taskService.getById(Long.MAX_VALUE));
    }

    @Test
    void getAllByAssigneeId() {
        long id = 1L;
        List<Task> expected = List.of(new Task()
            .setTitle("task title"));

        when(taskRepositoryMock.findAllByAssigneeId(id))
            .thenReturn(expected);
        List<Task> actual = taskService.getAllByAssigneeId(id);

        assertEquals(expected, actual);
    }

    @Test
    void getAllByAssigneeId_shouldReturnEmptyList() {
        long id = 1L;
        List<Task> emptyList = new ArrayList<>();

        when(taskRepositoryMock.findAllByAssigneeId(id))
            .thenReturn(emptyList);
        List<Task> actual = taskService.getAllByAssigneeId(id);

        assertEquals(emptyList, actual);
    }

    @Test
    void getAllByProjectId() {
        int pageNumber = 0;
        int pageSize = 2;
        long id = 1L;
        List<Task> expected = List.of(new Task()
            .setTitle("task title"));

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        when(taskRepositoryMock.findAllByProjectId(id, pageable))
            .thenReturn(new PageImpl<>(expected));
        List<Task> actual = taskService.getAllByProjectId(id, pageNumber, pageSize);

        assertEquals(expected, actual);
    }

    @Test
    void getAllByProjectId_shouldReturnEmptyList() {
        int pageNumber = 0;
        int pageSize = 2;
        long id = 1L;
        List<Task> emptyList = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        when(taskRepositoryMock.findAllByProjectId(id, pageable))
            .thenReturn(new PageImpl<>(emptyList));

        List<Task> actual = taskService.getAllByProjectId(id, pageNumber, pageSize);

        assertEquals(emptyList, actual);
    }


    @Test
    void update() {
        long id = 1L;
        Task newTask = new Task()
            .setTitle("new task title")
            .setDeadline(LocalDate.of(2000, 12, 12))
            .setPriority(Priority.HIGH)
            .setStatus(Status.IN_WORK)
            .setDescription("new task desc");
        Optional<Task> optionalTask = Optional.of(new Task()
            .setTitle("task title")
            .setDeadline(LocalDate.of(2000, 11, 11))
            .setPriority(Priority.NORMAL)
            .setStatus(Status.OPEN)
            .setDescription("task desc"));

        when(taskRepositoryMock.findById(id))
            .thenReturn(optionalTask);
        when(taskRepositoryMock.save(optionalTask.get()))
            .thenReturn(optionalTask.get());
        taskService.update(newTask, id);

        verify(taskRepositoryMock).findById(id);
        verify(taskRepositoryMock).save(newTask);
    }

    @Test
    void update_taskNotFound() {
        Task newTask = new Task()
            .setTitle("new task title")
            .setDeadline(LocalDate.of(2000, 12, 12))
            .setPriority(Priority.HIGH)
            .setStatus(Status.IN_WORK)
            .setDescription("new task desc");

        assertThrows(NotFoundException.class, () -> taskService.update(newTask, Long.MAX_VALUE));
    }

    @Test
    void deleteById() {
        long id = 1L;

        when(taskRepositoryMock.existsById(id))
            .thenReturn(true);
        taskService.deleteById(id);

        verify(taskRepositoryMock).deleteById(id);
    }

    @Test
    void deleteById_taskNotFound() {
        assertThrows(NotFoundException.class, () -> taskService.deleteById(Long.MAX_VALUE));
    }

    @Test
    void assignToUser() {
        long taskId = 2L;
        long userId = 1L;
        Optional<Task> optionalTask = Optional.of(new Task()
            .setTitle("task title")
            .setDeadline(LocalDate.of(2000, 12, 12))
            .setPriority(Priority.HIGH)
            .setStatus(Status.IN_WORK)
            .setDescription("task desc"));
        User user = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        when(userService.getById(userId))
            .thenReturn(user);

        when(taskRepositoryMock.findById(taskId))
            .thenReturn(optionalTask);
        Task task = optionalTask.get();

        when(taskRepositoryMock.save(task))
            .thenReturn(task);

        taskService.assignToUser(taskId, userId);

        assertThat(task.getAssignee()).isNotNull();
        assertEquals(task.getAssignee(), user);
        verify(taskRepositoryMock).save(task);
    }
}
