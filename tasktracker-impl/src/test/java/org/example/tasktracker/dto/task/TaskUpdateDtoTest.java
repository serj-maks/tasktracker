package org.example.tasktracker.dto.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import org.example.tasktracker.dto.task.TaskUpdateDto;
import org.example.tasktracker.dto.task.enums.Priority;
import org.example.tasktracker.dto.task.enums.Status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class TaskUpdateDtoTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void whenNotBlankName_thenNoConstraintViolation() {
        TaskUpdateDto dto = new TaskUpdateDto("Ivan", LocalDate.of(2000, 11, 11), Priority.NORMAL, Status.OPEN, "desc");
        Set<ConstraintViolation<TaskUpdateDto>> violations = validator.validate(dto);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void whenBlankName_thenOneConstraintViolation() {
        TaskUpdateDto dto = new TaskUpdateDto(" ", LocalDate.of(2000, 11, 11), Priority.NORMAL, Status.OPEN, "desc");
        Set<ConstraintViolation<TaskUpdateDto>> violations = validator.validate(dto);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void whenEmptyName_thenOneConstraintViolation() {
        TaskUpdateDto dto = new TaskUpdateDto("", LocalDate.of(2000, 11, 11), Priority.NORMAL, Status.OPEN, "desc");
        Set<ConstraintViolation<TaskUpdateDto>> violations = validator.validate(dto);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void whenNullName_thenOneConstraintViolation() {
        TaskUpdateDto dto = new TaskUpdateDto(null, LocalDate.of(2000, 11, 11), Priority.NORMAL, Status.OPEN, "desc");
        Set<ConstraintViolation<TaskUpdateDto>> violations = validator.validate(dto);

        assertThat(violations.size()).isEqualTo(1);
    }
}