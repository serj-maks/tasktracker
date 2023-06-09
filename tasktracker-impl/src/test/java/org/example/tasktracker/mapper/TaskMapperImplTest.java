package org.example.tasktracker.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import org.example.tasktracker.dto.task.TaskLiteResponseDto;
import org.example.tasktracker.dto.task.TaskResponseDto;
import org.example.tasktracker.Project;
import org.example.tasktracker.User;
import org.example.tasktracker.task.Task;
import org.example.tasktracker.task.enums.Priority;
import org.example.tasktracker.task.enums.Status;

public class TaskMapperImplTest {

    TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Test
    void toTaskLiteResponseDto() {
        Project project = new Project()
            .setId(1L)
            .setName("Sales")
            .setDescription("sales desc");
        User creator = new User()
            .setId(1L)
            .setName("Ivan")
            .setEmail("ivan@gmail.com");
        User assignee = new User()
            .setId(1L)
            .setName("Ivan")
            .setEmail("ivan@gmail.com");
        Task task = new Task()
            .setId(1L)
            .setDeadline(LocalDate.of(2000, 1, 1))
            .setTitle("task title")
            .setPriority(Priority.NORMAL)
            .setStatus(Status.OPEN)
            .setProject(project)
            .setCreator(creator)
            .setAssignee(assignee);

        TaskLiteResponseDto dto = taskMapper.toTaskLiteResponseDto(task);

        assertEquals(task.getId(), dto.id());
        assertEquals(task.getTitle(), dto.title());
    }

    @Test
    void toTasksLiteResponseDto() {
        Project project = new Project()
            .setId(1L)
            .setName("Sales")
            .setDescription("sales desc");
        User creator = new User()
            .setId(1L)
            .setName("Ivan")
            .setEmail("ivan@gmail.com");
        User assignee = new User()
            .setId(1L)
            .setName("Ivan")
            .setEmail("ivan@gmail.com");
        Task task = new Task()
            .setId(1L)
            .setDeadline(LocalDate.of(2000, 1, 1))
            .setTitle("task title")
            .setPriority(Priority.NORMAL)
            .setStatus(Status.OPEN)
            .setProject(project)
            .setCreator(creator)
            .setAssignee(assignee);
        List<TaskLiteResponseDto> actualDtos = taskMapper.toTasksLiteResponseDto(List.of(task));

        TaskLiteResponseDto expectedDto = new TaskLiteResponseDto(1L, "task title");
        List<TaskLiteResponseDto> expectedDtos = List.of(expectedDto);

        assertEquals(expectedDtos, actualDtos);
    }

    @Test
    void toTaskResponseDto() {
        Project project = new Project()
            .setId(1L)
            .setName("Sales")
            .setDescription("sales desc");
        User creator = new User()
            .setId(1L)
            .setName("Ivan")
            .setEmail("ivan@gmail.com");
        User assignee = new User()
            .setId(1L)
            .setName("Ivan")
            .setEmail("ivan@gmail.com");
        Task task = new Task()
            .setId(1L)
            .setDeadline(LocalDate.of(2000, 1, 1))
            .setTitle("task title")
            .setPriority(Priority.NORMAL)
            .setStatus(Status.OPEN)
            .setProject(project)
            .setCreator(creator)
            .setAssignee(assignee);

        TaskResponseDto taskResponseDto = taskMapper.toTaskResponseDto(task);

        assertEquals(task.getId(), taskResponseDto.id());
        assertEquals(org.example.tasktracker.dto.task.enums.Status.OPEN, taskResponseDto.status());
        assertEquals(org.example.tasktracker.dto.task.enums.Priority.NORMAL, taskResponseDto.priority());
        assertEquals(task.getDescription(), taskResponseDto.description());
        assertEquals(task.getProject().getId(), taskResponseDto.projectId());
    }
}
