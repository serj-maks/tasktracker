package org.example.tasktracker.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.example.tasktracker.IntegrationTest;
import org.example.tasktracker.dto.project.ProjectCreateDto;
import org.example.tasktracker.dto.project.ProjectResponseDto;
import org.example.tasktracker.dto.task.TaskResponseDto;
import org.example.tasktracker.dto.task.enums.Priority;
import org.example.tasktracker.dto.task.enums.Status;
import org.example.tasktracker.dto.user.UserResponseDto;
import org.example.tasktracker.Project;
import org.example.tasktracker.User;
import org.example.tasktracker.task.Task;

public class ProjectResourceImplTest extends IntegrationTest {

    @Test
    void getAll() {
        Project project1 = new Project()
            .setName("Sales")
            .setDescription("sales desc");

        Project project2 = new Project()
            .setName("HR")
            .setDescription("hr desc");

        project1 = projectRepository.save(project1);
        project2 = projectRepository.save(project2);

        ResponseEntity<List<ProjectResponseDto>> response = restTemplate.exchange(
            "/api/projects",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ProjectResponseDto>>() {
            }
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        ProjectResponseDto responseDtoProject1 = new ProjectResponseDto(project1.getId(), project1.getName(), project1.getDescription());
        ProjectResponseDto responseDtoProject2 = new ProjectResponseDto(project2.getId(), project2.getName(), project2.getDescription());
        List<ProjectResponseDto> actual = response.getBody();

        assertThat(actual).containsExactlyInAnyOrder(responseDtoProject1, responseDtoProject2);
    }

    @Test
    void getById() {
        Project project = new Project()
            .setName("Sales")
            .setDescription("sales desc");

        project = projectRepository.save(project);

        ResponseEntity<ProjectResponseDto> response = restTemplate.exchange(
            "/api/projects/{id}",
            HttpMethod.GET,
            null,
            ProjectResponseDto.class,
            Map.of("id", project.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        ProjectResponseDto actual = response.getBody();
        ProjectResponseDto expected = new ProjectResponseDto(project.getId(), project.getName(), project.getDescription());
        assertEquals(expected, actual);
    }

    @Test
    void getById_shouldReturn404Response() {
        ResponseEntity<ProjectResponseDto> response = restTemplate.exchange(
            "/api/projects/{id}",
            HttpMethod.GET,
            null,
            ProjectResponseDto.class,
            Map.of("id", Long.MAX_VALUE)
        );

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllTasks() {
        User creator = new User()
            .setName("Aleksandr")
            .setEmail("alex@gmail.com");

        User assignee = new User()
            .setName("Ivan")
            .setEmail("ivan@gmail.com");

        Task task = new Task()
            .setTitle("task title")
            .setPriority(org.example.tasktracker.task.enums.Priority.NORMAL)
            .setStatus(org.example.tasktracker.task.enums.Status.OPEN)
            .setDescription("task desc")
            .setAssignee(assignee)
            .setCreator(creator);

        Project project = new Project()
            .setName("sales")
            .setDescription("sales desc");

        task.setProject(project);
        taskRepository.save(task);
        projectRepository.save(project);

        ResponseEntity<List<TaskResponseDto>> response = restTemplate.exchange(
            "/api/projects/{id}/tasks?page={page}&size={size}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<TaskResponseDto>>() {
            },
            Map.of("id", project.getId(), "page", 0, "size", 2)
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        List<TaskResponseDto> actual = response.getBody();
        TaskResponseDto expected = new TaskResponseDto(response.getBody().get(0).id(), Status.OPEN, Priority.NORMAL, "task desc", project.getId());
        assertThat(actual).isNotEmpty();
        assertEquals(List.of(expected), actual);
    }

    @Test
    void getAllTasks_taskListShouldBeEmpty() {
        ResponseEntity<List<TaskResponseDto>> response = restTemplate.exchange(
            "/api/projects/{id}/tasks?page={page}&size={size}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<TaskResponseDto>>() {
            },
            Map.of("id", Long.MAX_VALUE, "page", 0, "size", 2)
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        List<TaskResponseDto> actual = response.getBody();
        assertThat(actual).isEmpty();
    }

    @Test
    void create() {
        ResponseEntity<ProjectResponseDto> response = restTemplate.exchange(
            "/api/projects",
            HttpMethod.POST,
            new HttpEntity<>(new ProjectCreateDto("Sales", "sales desc")),
            ProjectResponseDto.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertTrue(projectRepository.existsById(response.getBody().id()));
    }

    @Test
    void create_addProjectWithSameName_returnBadRequest() {
        Project project = new Project()
            .setName("Sales")
            .setDescription("sales desc");

        projectRepository.save(project);

        ResponseEntity<ProjectResponseDto> response = restTemplate.exchange(
            "/api/projects",
            HttpMethod.POST,
            new HttpEntity<>(new ProjectCreateDto("Sales", "sales desc 2")),
            ProjectResponseDto.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void getUsersFromProject() {
        User user = new User()
            .setName("Ivan")
            .setEmail("123@gmail.com");
        Project project = new Project()
            .setName("Sales");

        project.addUser(user);
        project = projectRepository.save(project);

        ResponseEntity<List<UserResponseDto>> response = restTemplate.exchange(
            "/api/projects/{id}/users",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<UserResponseDto>>() {
            },
            Map.of("id", project.getId())
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        List<UserResponseDto> actual = response.getBody();
        UserResponseDto userResponseDto = new UserResponseDto(user.getId(), user.getName(), user.getEmail());
        assertThat(actual).contains(userResponseDto);
    }
}
