package edu.bbte.idde.seim1964.spring.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
@AllArgsConstructor
public class WrongPathException extends RuntimeException {
    private String message;
}