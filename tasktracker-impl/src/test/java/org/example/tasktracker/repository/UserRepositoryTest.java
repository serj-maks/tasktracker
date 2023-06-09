package org.example.tasktracker.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import org.example.tasktracker.IntegrationTest;
import org.example.tasktracker.Project;
import org.example.tasktracker.User;

class UserRepositoryTest extends IntegrationTest {

    @Test
    void save() {
        transactionTemplate.executeWithoutResult(s -> {
            User user = user("Ivan", "123@gmail.com");
            User savedUser = userRepository.save(user);
            User actualUser = userRepository.findById(savedUser.getId()).get();
            assertEquals(savedUser, actualUser);
        });
    }

    @Test
    void save_shouldNotSaveDuplicateName() {
        userRepository.save(user("Ivan", "123@gmail.com"));
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user("Ivan", "456@gmail.com")));
    }

    @Test
    void save_shouldNotSaveDuplicateEmail() {
        userRepository.save(user("Ivan", "123@gmail.com"));
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user("Alex", "123@gmail.com")));
    }

    @Test
    void findByProjectsId() {
        User savedUser = userRepository.save(user("Ivan", "123@gmail.com"));
        Project project = new Project()
            .setName("Sales");
        projectRepository.save(project);
        project.addUser(savedUser);
        projectRepository.save(project);
        List<User> result = userRepository.findByProjectsId(project.getId());
        assertFalse(result.isEmpty());
    }

    @Test
    void findByProjectsId_shouldBeEmpty() {
        List<User> result = userRepository.findByProjectsId(Long.MAX_VALUE);
        assertTrue(result.isEmpty());
    }

    private static User user(String name, String email) {
        return new User()
            .setName(name)
            .setEmail(email);
    }
}
