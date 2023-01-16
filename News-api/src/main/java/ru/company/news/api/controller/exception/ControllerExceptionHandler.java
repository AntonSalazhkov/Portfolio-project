package ru.company.news.api.controller.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.company.news.api.dto.exception.response.ResponseError;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Exception handling controller.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */

@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handling exceptions for not finding an entity in the database.
     *
     * @param e the resulting exception.
     * @return response including current time, message and exception name.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseError handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseError(getNowLocalDateTime(), e.getMessage(), e.toString());
    }

    /**
     * Handling data validation exceptions in received requests.
     *
     * @param e the resulting exception.
     * @return response including current time, message and exception name.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseError(getNowLocalDateTime(), e.getMessage(), e.toString());
    }

    /**
     * Handling exceptions for missing required parameters in received requests.
     *
     * @param e the resulting exception.
     * @return response including current time, message and exception name.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseError handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new ResponseError(getNowLocalDateTime(), e.getMessage(), e.toString());
    }

    /**
     * Handling value type mismatch exceptions in received requests.
     *
     * @param e the resulting exception.
     * @return response including current time, message and exception name.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseError handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new ResponseError(getNowLocalDateTime(), e.getMessage(), e.toString());
    }

    /**
     * Handling exceptions for invalid request parameter formats.
     *
     * @param e the resulting exception.
     * @return response including current time, message and exception name.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseError handleInvalidFormatException(InvalidFormatException e) {
        return new ResponseError(getNowLocalDateTime(), e.getMessage(), e.toString());
    }

    private String getNowLocalDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }
}
