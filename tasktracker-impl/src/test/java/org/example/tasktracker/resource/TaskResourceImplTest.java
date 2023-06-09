package org.example.tasktracker.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.example.tasktracker.IntegrationTest;
import org.example.tasktracker.dto.task.TaskCreateDto;
import org.example.tasktracker.dto.task.TaskLiteResponseDto;
import org.example.tasktracker.dto.task.TaskResponseDto;
import org.example.tasktracker.dto.task.TaskUpdateDto;
import org.example.tasktracker.dto.task.enums.Priority;
import org.example.tasktracker.dto.task.enums.Status;
import org.example.tasktracker.Project;
import org.example.tasktracker.User;
import org.example.tasktracker.task.Task;

class TaskResourceImplTest extends IntegrationTest {

    @Test
    void getAll() {
        Project project1 = new Project()
            .setName("Sales")
            .setDescription("sales desc");
        User creator1 = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");
        Task task1 = new Task()
            .setTitle("task1 title")
            .setCreator(creator1)
            .setProject(project1);

        Project project2 = new Project()
            .setName("Develop")
            .setDescription("develop desc");
        User creator2 = new User()
            .setName("Motvey")
            .setEmail("motvey@gmail.com");
        Task task2 = new Task()
            .setTitle("task2 title")
            .setCreator(creator2)
            .setProject(project2);

        task1 = taskRepository.save(task1);
        task2 = taskRepository.save(task2);

        ResponseEntity<List<TaskLiteResponseDto>> response = restTemplate.exchange(
            "/api/tasks",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<TaskLiteResponseDto>>() {
            }
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        TaskLiteResponseDto responseDtoTask1 = new TaskLiteResponseDto(task1.getId(), task1.getTitle());
        TaskLiteResponseDto responseDtoTask2 = new TaskLiteResponseDto(task2.getId(), task2.getTitle());
        List<TaskLiteResponseDto> actual = response.getBody();

        assertThat(actual).containsExactlyInAnyOrder(responseDtoTask1, responseDtoTask2);
    }

    @Test
    void getById() {
        Project project = new Project()
            .setName("Sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setCreator(creator)
            .setProject(project);

        task = taskRepository.save(task);

        ResponseEntity<TaskResponseDto> response = restTemplate.exchange(
            "/api/tasks/{id}",
            HttpMethod.GET,
            null,
            TaskResponseDto.class,
            Map.of("id", task.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        TaskResponseDto actual = response.getBody();
        TaskResponseDto expected = new TaskResponseDto(
            task.getId(),
//          т.к. в api-модуле продублированы enum-классы, task.getStatus() заменен на хардкод
            Status.OPEN,
            Priority.NORMAL,
            task.getDescription(),
            task.getProject().getId());

        assertEquals(expected, actual);
    }

    @Test
    void create() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        projectRepository.save(project);
        userRepository.save(creator);

        ResponseEntity<TaskResponseDto> response = restTemplate.exchange(
            "/api/tasks",
            HttpMethod.POST,
            new HttpEntity<>(new TaskCreateDto("task title", project.getId(), creator.getId())),
            TaskResponseDto.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertTrue(taskRepository.existsById(response.getBody().id()));

        TaskResponseDto expected = new TaskResponseDto(response.getBody().id(), Status.OPEN, Priority.NORMAL, null, project.getId());
        TaskResponseDto actual = response.getBody();
        assertEquals(expected, actual);
    }

    @Test
    void create_404becauseProjectNotFound() {
        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        userRepository.save(creator);

        //в дженерике использую String вместо TaskResponseDto, чтобы получить HttpStatus.NOT_FOUND
        //выбор дженерика на результат теста не влияет
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/tasks",
            HttpMethod.POST,
            new HttpEntity<>(new TaskCreateDto("task title", Long.MAX_VALUE, creator.getId())),
            String.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void create_404becauseCreatorNotFound() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        projectRepository.save(project);

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/tasks",
            HttpMethod.POST,
            new HttpEntity<>(new TaskCreateDto("task title", project.getId(), Long.MAX_VALUE)),
            String.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void update() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setDeadline(LocalDate.of(2000, 11, 11))
            .setPriority(org.example.tasktracker.task.enums.Priority.NORMAL)
            .setStatus(org.example.tasktracker.task.enums.Status.OPEN)
            .setDescription("task desc")
            .setProject(project)
            .setCreator(creator);

        task = taskRepository.save(task);

        ResponseEntity<TaskResponseDto> response = restTemplate.exchange(
            "/api/tasks/{id}",
            HttpMethod.PUT,
            new HttpEntity<>(new TaskUpdateDto("new task title", LocalDate.of(2001, 12, 12), Priority.HIGH, Status.IN_WORK, "new task desc")),
            TaskResponseDto.class,
            Map.of("id", task.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        TaskResponseDto expected = new TaskResponseDto(response.getBody().id(), Status.IN_WORK, Priority.HIGH, "new task desc", project.getId());
        TaskResponseDto actual = response.getBody();
        assertEquals(expected, actual);
    }

    @Test
    void update_404becauseTaskNotFound() {
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/tasks/{id}",
            HttpMethod.PUT,
            new HttpEntity<>(new TaskUpdateDto("new task title", LocalDate.of(2001, 12, 12), Priority.HIGH, Status.IN_WORK, "new task desc")),
            String.class,
            Map.of("id", Long.MAX_VALUE)
        );

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void update_notBlank_validTitle() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setDeadline(LocalDate.of(2000, 11, 11))
            .setPriority(org.example.tasktracker.task.enums.Priority.NORMAL)
            .setStatus(org.example.tasktracker.task.enums.Status.OPEN)
            .setDescription("task desc")
            .setProject(project)
            .setCreator(creator);

        task = taskRepository.save(task);

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/tasks/{id}",
            HttpMethod.PUT,
            new HttpEntity<>(new TaskUpdateDto("new task title", LocalDate.of(2001, 12, 12), Priority.HIGH, Status.IN_WORK, "new task desc")),
            String.class,
            Map.of("id", task.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void update_notBlank_blankTitle() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setDeadline(LocalDate.of(2000, 11, 11))
            .setPriority(org.example.tasktracker.task.enums.Priority.NORMAL)
            .setStatus(org.example.tasktracker.task.enums.Status.OPEN)
            .setDescription("task desc")
            .setProject(project)
            .setCreator(creator);

        task = taskRepository.save(task);

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/tasks/{id}",
            HttpMethod.PUT,
            new HttpEntity<>(new TaskUpdateDto(" ", LocalDate.of(2001, 12, 12), Priority.HIGH, Status.IN_WORK, "new task desc")),
            String.class,
            Map.of("id", task.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void update_notBlank_emptyTitle() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setDeadline(LocalDate.of(2000, 11, 11))
            .setPriority(org.example.tasktracker.task.enums.Priority.NORMAL)
            .setStatus(org.example.tasktracker.task.enums.Status.OPEN)
            .setDescription("task desc")
            .setProject(project)
            .setCreator(creator);

        task = taskRepository.save(task);

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/tasks/{id}",
            HttpMethod.PUT,
            new HttpEntity<>(new TaskUpdateDto("", LocalDate.of(2001, 12, 12), Priority.HIGH, Status.IN_WORK, "new task desc")),
            String.class,
            Map.of("id", task.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void update_notBlank_nullTitle() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setDeadline(LocalDate.of(2000, 11, 11))
            .setPriority(org.example.tasktracker.task.enums.Priority.NORMAL)
            .setStatus(org.example.tasktracker.task.enums.Status.OPEN)
            .setDescription("task desc")
            .setProject(project)
            .setCreator(creator);

        task = taskRepository.save(task);

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/tasks/{id}",
            HttpMethod.PUT,
            new HttpEntity<>(new TaskUpdateDto(null, LocalDate.of(2001, 12, 12), Priority.HIGH, Status.IN_WORK, "new task desc")),
            String.class,
            Map.of("id", task.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteById() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setDeadline(LocalDate.of(2000, 11, 11))
            .setPriority(org.example.tasktracker.task.enums.Priority.NORMAL)
            .setStatus(org.example.tasktracker.task.enums.Status.OPEN)
            .setDescription("task desc")
            .setProject(project)
            .setCreator(creator);

        task = taskRepository.save(task);

        ResponseEntity<TaskResponseDto> response = restTemplate.exchange(
            "/api/tasks/{id}",
            HttpMethod.DELETE,
            null,
            TaskResponseDto.class,
            Map.of("id", task.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }

    @Test
    void assignToUser() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setDeadline(LocalDate.of(2000, 11, 11))
            .setPriority(org.example.tasktracker.task.enums.Priority.NORMAL)
            .setStatus(org.example.tasktracker.task.enums.Status.OPEN)
            .setDescription("task desc")
            .setProject(project)
            .setCreator(creator);

        User user = new User()
            .setName("Aleksandr")
            .setEmail("alex@gmail.com");

        Task savedTask = taskRepository.save(task);
        User savedUser = userRepository.save(user);

        ResponseEntity<TaskResponseDto> response = restTemplate.exchange(
            "/api/tasks/{taskId}/users/{userId}",
            HttpMethod.PUT,
            null,
            TaskResponseDto.class,
            Map.of("taskId", savedTask.getId(), "userId", savedUser.getId())
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        transactionTemplate.executeWithoutResult(s -> {
            Task actualTask = taskRepository.findById(savedTask.getId()).get();
            assertThat(actualTask.getAssignee()).isNotNull();
        });
    }

    @Test
    void assignToUser_taskNotFound() {
        User user = new User()
            .setName("Aleksandr")
            .setEmail("alex@gmail.com");

        user = userRepository.save(user);

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/tasks/{taskId}/users/{userId}",
            HttpMethod.PUT,
            null,
            String.class,
            Map.of("taskId", Long.MAX_VALUE, "userId", user.getId())
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void assignToUser_userNotFound() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setDeadline(LocalDate.of(2000, 11, 11))
            .setPriority(org.example.tasktracker.task.enums.Priority.NORMAL)
            .setStatus(org.example.tasktracker.task.enums.Status.OPEN)
            .setDescription("task desc")
            .setProject(project)
            .setCreator(creator);

        task = taskRepository.save(task);

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/tasks/{taskId}/users/{userId}",
            HttpMethod.PUT,
            null,
            String.class,
            Map.of("taskId", task.getId(), "userId", Long.MAX_VALUE)
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
