package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.io.IOException;
import java.util.Map;

@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ExceptionHandle {
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> errorValidation(final ValidationException e) {
        return new ResponseEntity<>(
                Map.of("Ошибка валидации", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler // внедрить это исключение в Film
    public ResponseEntity<Map<String, String>> objectNotFound(final ObjectNotFoundException e) {
        return new ResponseEntity<>(
                Map.of("Объект не найден", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> appearedException(final IOException e) {
        return new ResponseEntity<>(
                Map.of("Возникло исключение", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
