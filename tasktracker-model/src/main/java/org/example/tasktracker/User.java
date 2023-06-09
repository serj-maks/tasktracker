package org.example.tasktracker;

import java.util.ArrayList;
import java.util.List;

import org.example.tasktracker.task.Task;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Table(name = "\"user\"", schema = "public")
@Entity
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @Column
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    Long id;

    @Column
    String name;

    @Column
    String email;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    List<Task> createdTasks = new ArrayList<>();

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    List<Task> assignedTasks = new ArrayList<>();
}
