package com.firstacademy.firstblock.exception;

import com.firstacademy.firstblock.dto.response.Response;

import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.SubmitException;
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

    // EndorseException,SubmitException,CommitStatusException,CommitException

    @ExceptionHandler(EndorseException.class)
    public final ResponseEntity<Response<Object>> handleEndorseException(Exception ex, WebRequest request) {
        Response<Object> response = Response.badRequest();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<Response<Object>>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SubmitException.class)
    public final ResponseEntity<Response<Object>> handleSubmitException(Exception ex, WebRequest request) {
        Response<Object> response = Response.badRequest();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<Response<Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommitException.class)
    public final ResponseEntity<Response<Object>> handleCommitException(Exception ex, WebRequest request) {
        Response<Object> response = Response.badRequest();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<Response<Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommitStatusException.class)
    public final ResponseEntity<Response<Object>> handleCommitStatusException(Exception ex, WebRequest request) {
        Response<Object> response = Response.badRequest();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<Response<Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response<Object>> handleAllUnhandledExceptions(Exception ex, WebRequest request) {
        Response<Object> response = Response.exception();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<Response<Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
