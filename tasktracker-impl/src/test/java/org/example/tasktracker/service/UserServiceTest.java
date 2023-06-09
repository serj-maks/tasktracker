package org.example.tasktracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.tasktracker.exception.AlreadyExistsException;
import org.example.tasktracker.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.tasktracker.Project;
import org.example.tasktracker.User;
import org.example.tasktracker.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserService userService;

    @Mock
    private ProjectService projectService;

    @AfterEach
    void tearDown() {
        reset(userRepositoryMock);
    }

    @Test
    void getById_returnUser() {
        long id = 1L;
        User expected = new User()
            .setId(id)
            .setName("Ivan")
            .setEmail("123@gmail.com");

        when(userRepositoryMock.findById(id))
            .thenReturn(Optional.of(expected));
        User actual = userService.getById(id);

        assertEquals(expected, actual);
        verify(userRepositoryMock).findById(id);
    }

    @Test
    void getById_throwException() {
        long id = 1L;

        assertThrows(NotFoundException.class, () -> userService.getById(id));
    }

    @Test
    void save() {
        User user = new User()
            .setName("Ivan")
            .setEmail("123@gmail.com");

        when(userRepositoryMock.save(user))
            .thenReturn(user);
        User savedUser = userService.create(user);

        assertEquals(user.getName(), savedUser.getName());
        verify(userRepositoryMock).save(user);
    }

    @Test
    void save_whenUsersHasEqualNames_thenThrowException() {
        User user = new User()
            .setName("Ivan")
            .setEmail("123@gmail.com");

        when(userRepositoryMock.existsByName(user.getName()))
            .thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.create(user));
    }

    @Test
    void save_whenUsersHasEqualEmails_thenThrowException() {
        User user = new User()
            .setName("Ivan")
            .setEmail("123@gmail.com");

        when(userRepositoryMock.existsByEmail(user.getEmail()))
            .thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.create(user));
    }

    @Test
    void getByProjectId() {
        long projectId = 1L;
        User user = new User()
            .setName("Ivan")
            .setEmail("123@gmail.com");
        List<User> expectedList = List.of(user);

        when(userRepositoryMock.findByProjectsId(projectId))
            .thenReturn(expectedList);
        List<User> actualList = userService.getByProjectId(projectId);

        assertEquals(expectedList, actualList);
    }

    @Test
    void addToProject() {
        long projectId = 2L;
        long userId = 1L;
        Project project = new Project()
            .setName("Sales");
        Optional<User> optionalUser = Optional.of(new User()
            .setName("Ivan")
            .setEmail("123@gmail.com"));

        when(projectService.getById(projectId))
            .thenReturn(project);

        when(userRepositoryMock.findById(userId))
            .thenReturn(optionalUser);
        User user = optionalUser.get();

        when(userRepositoryMock.save(user))
            .thenReturn(user);

        userService.addToProject(userId, projectId);

        assertThat(project.getUsers()).isNotEmpty();
        verify(userRepositoryMock).save(user);
    }
}
