package org.example.tasktracker.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.example.tasktracker.task.enums.Priority;
import org.example.tasktracker.task.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.example.tasktracker.IntegrationTest;
import org.example.tasktracker.dto.task.TaskResponseDto;
import org.example.tasktracker.dto.user.UserCreateDto;
import org.example.tasktracker.dto.user.UserResponseDto;
import org.example.tasktracker.Project;
import org.example.tasktracker.User;
import org.example.tasktracker.task.Task;

class UserResourceImplTest extends IntegrationTest {

    @Test
    void getAll() {
        User user1 = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");
        User user2 = new User()
            .setName("Aleksandr")
            .setEmail("alex@gmail.com");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        ResponseEntity<List<UserResponseDto>> response = restTemplate.exchange(
            "/api/users",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<UserResponseDto>>() {
            }
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        UserResponseDto responseDtoUser1 = new UserResponseDto(user1.getId(), user1.getName(), user1.getEmail());
        UserResponseDto responseDtoUser2 = new UserResponseDto(user2.getId(), user2.getName(), user2.getEmail());
        List<UserResponseDto> actual = response.getBody();

        assertThat(actual).containsExactlyInAnyOrder(responseDtoUser1, responseDtoUser2);
    }

    @Test
    void getById() {
        User user = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        user = userRepository.save(user);

        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
            "/api/users/{id}",
            HttpMethod.GET,
            null,
            UserResponseDto.class,
            Map.of("id", user.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        UserResponseDto actual = response.getBody();
        UserResponseDto expected = new UserResponseDto(user.getId(), user.getName(), user.getEmail());
        assertEquals(expected, actual);
    }

    @Test
    void getById_shouldReturn404Response() {
        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
            "/api/users/{id}",
            HttpMethod.GET,
            null,
            UserResponseDto.class,
            Map.of("id", Long.MAX_VALUE)
        );
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void create() {
        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
            "/api/users",
            HttpMethod.POST,
            new HttpEntity<>(new UserCreateDto("Ivan", "123@gmail.com")),
            UserResponseDto.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertTrue(userRepository.existsById(response.getBody().id()));
    }

    @Test
    void create_addUserWithSameName_returnBadRequest() {
        User user = new User()
            .setName("Ivan")
            .setEmail("123@gmail.com");

        userRepository.save(user);

        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
            "/api/users",
            HttpMethod.POST,
            new HttpEntity<>(new UserCreateDto("Ivan", "456@gmail.com")),
            UserResponseDto.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void create_addUserWithSameEmail_returnBadRequest() {
        User user = new User()
            .setName("Ivan")
            .setEmail("123@gmail.com");

        userRepository.save(user);

        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
            "/api/users",
            HttpMethod.POST,
            new HttpEntity<>(new UserCreateDto("Alex", "123@gmail.com")),
            UserResponseDto.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void create_badRequestWithIncorrectEmail() {
        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
            "/api/users",
            HttpMethod.POST,
            new HttpEntity<>(new UserCreateDto("Ivan", "456.com")),
            UserResponseDto.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void getAllAssignedTasks() {
        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        User creator = new User()
            .setName("Aleksandr")
            .setEmail("alex@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setPriority(Priority.NORMAL)
            .setStatus(Status.OPEN)
            .setDescription("task desc")
            .setProject(project)
            .setCreator(creator);

        User assignee = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        task.setAssignee(assignee);
        taskRepository.save(task);
        userRepository.save(assignee);

        ResponseEntity<List<TaskResponseDto>> response = restTemplate.exchange(
            "/api/users/{id}/tasks",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<TaskResponseDto>>() {
            },
            Map.of("id", assignee.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        transactionTemplate.executeWithoutResult(s -> {
            List<TaskResponseDto> actual = response.getBody();
            assertThat(actual).isNotEmpty();
        });
    }

    @Test
    void getAllAssignedTasks_taskListShouldBeEmpty() {
        ResponseEntity<List<TaskResponseDto>> response = restTemplate.exchange(
            "/api/users/{id}/tasks",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<TaskResponseDto>>() {
            },
            Map.of("id", Long.MAX_VALUE)
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        transactionTemplate.executeWithoutResult(s -> {
            List<TaskResponseDto> actual = response.getBody();
            assertThat(actual).isEmpty();
        });
    }

    @Test
    void addToProject() {
        User user = new User()
            .setName("Ivan")
            .setEmail("123@gmail.com");
        Project project = new Project()
            .setName("Sales");

        User savedUser = userRepository.save(user);
        Project savedProject = projectRepository.save(project);

        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
            "/api/users/{userId}/projects/{projectId}",
            HttpMethod.PUT,
            null,
            UserResponseDto.class,
            Map.of("userId", savedUser.getId(), "projectId", savedProject.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        transactionTemplate.executeWithoutResult(s -> {
            User actualUser = userRepository.findById(savedUser.getId()).get();
            assertThat(actualUser.getProjects()).isNotEmpty();
        });
    }

    @Test
    void addToProject_projectNotFound() {
        User user = new User()
            .setName("Ivan")
            .setEmail("123@gmail.com");

        User savedUser = userRepository.save(user);

        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
            "/api/users/{userId}/projects/{projectId}",
            HttpMethod.PUT,
            null,
            UserResponseDto.class,
            Map.of("userId", savedUser.getId(), "projectId", Long.MAX_VALUE)
        );

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void addToProject_userNotFound() {
        Project project = new Project()
            .setName("Sales");

        Project savedProject = projectRepository.save(project);

        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
            "/api/users/{userId}/projects/{projectId}",
            HttpMethod.PUT,
            null,
            UserResponseDto.class,
            Map.of("userId", Long.MAX_VALUE, "projectId", savedProject.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
