package org.example.tasktracker;

import org.example.tasktracker.repository.ProjectRepository;
import org.example.tasktracker.repository.TaskRepository;
import org.example.tasktracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected TransactionTemplate transactionTemplate;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected TaskRepository taskRepository;

    @AfterEach
    final void clean() {
        userRepository.deleteAll();
        projectRepository.deleteAll();
        taskRepository.deleteAll();
    }
}
