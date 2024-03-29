package com.firstacademy.firstblock.exception;

import com.firstacademy.firstblock.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomExceptions.EntityNotFoundException.class)
    public final ResponseEntity<Response<Object>> handleNotFountExceptions(Exception ex, WebRequest request) {
        Response<Object> response = Response.notFound();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<Response<Object>>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.DuplicateEntityException.class)
    public final ResponseEntity<Response<Object>> handleNotFountExceptions1(Exception ex, WebRequest request) {
        Response<Object> response = Response.duplicateEntity();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<Response<Object>>(response, HttpStatus.CONFLICT);
    }
}
