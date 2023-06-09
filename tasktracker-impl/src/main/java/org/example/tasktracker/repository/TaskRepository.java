package org.example.tasktracker.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.tasktracker.task.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsById(Long id);

    List<Task> findAllByAssigneeId(Long id);

    Page<Task> findAllByProjectId(Long id, Pageable pageable);
}
