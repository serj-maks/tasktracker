package org.example.tasktracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.tasktracker.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    List<User> findByProjectsId(Long projectId);
}
