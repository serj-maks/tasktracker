package org.example.tasktracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(Class<?> entity, String subject) {
        super(String.format("'%s' with '%s' already exists", entity.getSimpleName(), subject));
    }
}
