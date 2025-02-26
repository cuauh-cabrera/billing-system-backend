package com.cbm.billing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ForbiddenOperationException extends Exception{

    public ForbiddenOperationException() {
    }

    public ForbiddenOperationException(String message) {
        super(message, null, true, false);
    }
}
