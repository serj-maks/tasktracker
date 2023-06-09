package org.example.tasktracker.service;

import java.util.List;

import org.example.tasktracker.exception.AlreadyExistsException;
import org.example.tasktracker.exception.NotFoundException;
import org.example.tasktracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.example.tasktracker.Project;
import org.example.tasktracker.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProjectService projectService;

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(User.class, id));
    }

    @Transactional(readOnly = true)
    public List<User> getByProjectId(long projectId) {
        return userRepository.findByProjectsId(projectId);
    }

    @Transactional
    public User create(User user) throws AlreadyExistsException {
        if (userRepository.existsByName(user.getName())) {
            throw new AlreadyExistsException(User.class, user.getName());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException(User.class, user.getEmail());
        }
        return userRepository.save(user);
    }

    @Transactional
    public void addToProject(long userId, long projectId) {
        Project project = projectService.getById(projectId);
        User user = getById(userId);
        project.addUser(user);
        // update в бд
        userRepository.save(user);
    }
}
