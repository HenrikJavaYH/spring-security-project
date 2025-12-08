package org.henrikjavayh.springsecurity.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, int status) {
}
