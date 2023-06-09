package org.example.tasktracker.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.example.tasktracker.IntegrationTest;
import org.example.tasktracker.Project;
import org.example.tasktracker.User;
import org.example.tasktracker.task.Task;
import org.example.tasktracker.task.enums.Priority;
import org.example.tasktracker.task.enums.Status;

class TaskRepositoryTest extends IntegrationTest {

    @Test
    void save() {
        transactionTemplate.executeWithoutResult(s -> {
            User user = user("Ivan", "123@gmail.com");
            user = userRepository.save(user);

            Project project = project("Sales", "sales desc");
            project = projectRepository.save(project);

            Task task = task("do something", user);

            task.setProject(project);
            task = taskRepository.save(task);

            assertEquals(task.getProject(), project);
            assertEquals(task.getPriority(), Priority.NORMAL);
            assertEquals(task.getStatus(), Status.OPEN);
        });
    }

    @Test
    void deleteById() {
        transactionTemplate.executeWithoutResult(s -> {
            User user = user("Ivan", "123@gmail.com");
            user = userRepository.save(user);

            Project project = project("Sales", "sales desc");
            project = projectRepository.save(project);

            Task task = task("do something", user);

            task.setProject(project);
            taskRepository.save(task);

            boolean isExistBeforeDelete = taskRepository.findById(task.getId()).isPresent();
            taskRepository.deleteById(task.getId());
            boolean notExistAfterDelete = taskRepository.findById(task.getId()).isPresent();

            assertTrue(isExistBeforeDelete);
            assertFalse(notExistAfterDelete);
        });
    }

    @Test
    void findByAssigneeId() {
        transactionTemplate.executeWithoutResult(s -> {
            Project project1 = new Project()
                .setName("sales")
                .setDescription("sales desc");
            User creator1 = new User()
                .setName("Ivan")
                .setEmail("ivan@gmail.com");
            Task task1 = new Task()
                .setTitle("task1 title")
                .setDeadline(LocalDate.of(2000, 11, 11))
                .setPriority(Priority.NORMAL)
                .setStatus(Status.OPEN)
                .setDescription("task1 desc")
                .setProject(project1)
                .setCreator(creator1);

            Project project2 = new Project()
                .setName("rd")
                .setDescription("rd desc");
            User creator2 = new User()
                .setName("Petya")
                .setEmail("petya@gmail.com");
            Task task2 = new Task()
                .setTitle("task2 title")
                .setDeadline(LocalDate.of(2000, 11, 11))
                .setPriority(Priority.NORMAL)
                .setStatus(Status.OPEN)
                .setDescription("task2 desc")
                .setProject(project2)
                .setCreator(creator2);

            User assignee = new User()
                .setName("Alex")
                .setEmail("alex@gmail.com");

            task1.setAssignee(assignee);
            task2.setAssignee(assignee);
            taskRepository.save(task1);
            taskRepository.save(task2);
            List<Task> actual = taskRepository.findAllByAssigneeId(assignee.getId());

            assertEquals(List.of(task1, task2), actual);
        });
    }

    @Test
    void findAllTasksByProjectId() {
        transactionTemplate.executeWithoutResult(s -> {
            Project project = new Project()
                .setName("sales")
                .setDescription("sales desc");

            User creator1 = new User()
                .setName("Ivan")
                .setEmail("ivan@gmail.com");
            Task task1 = new Task()
                .setTitle("task1 title")
                .setDeadline(LocalDate.of(2000, 11, 11))
                .setPriority(Priority.NORMAL)
                .setStatus(Status.OPEN)
                .setDescription("task1 desc")
                .setCreator(creator1);

            User creator2 = new User()
                .setName("Petya")
                .setEmail("petya@gmail.com");
            Task task2 = new Task()
                .setTitle("task2 title")
                .setDeadline(LocalDate.of(2000, 11, 11))
                .setPriority(Priority.NORMAL)
                .setStatus(Status.OPEN)
                .setDescription("task2 desc")
                .setCreator(creator2);

            task1.setProject(project);
            task2.setProject(project);
            taskRepository.save(task1);
            taskRepository.save(task2);

            Pageable pageable = PageRequest.of(0, 2);

            Page<Task> actual = taskRepository.findAllByProjectId(project.getId(), pageable);

            assertEquals(List.of(task1, task2), actual.getContent());
        });
    }

    private static User user(String name, String email) {
        return new User()
            .setName(name)
            .setEmail(email);
    }

    private static Project project(String name, String description) {
        return new Project()
            .setName(name)
            .setDescription(description);
    }

    private static Task task(String title, User creator) {
        return new Task()
            .setTitle(title)
            .setCreator(creator);
    }
}