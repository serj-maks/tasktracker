package org.example.tasktracker.task;

import java.time.LocalDate;

import org.example.tasktracker.Project;
import org.example.tasktracker.User;
import org.example.tasktracker.task.enums.Priority;
import org.example.tasktracker.task.enums.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
public class Task {

    @Id
    @Column
    @SequenceGenerator(name = "task_id_seq", sequenceName = "task_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_seq")
    private Long id;

    @Column
    private String title;

    @Column
    private LocalDate deadline;

    @Column
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    /*
    код специально для JPA, чтобы работали default-значения
     */
    @PrePersist
    public void fillDefaults() {
        if (priority == null) {
            priority = Priority.NORMAL;
        }
        if (status == null) {
            status = Status.OPEN;
        }
    }
}
