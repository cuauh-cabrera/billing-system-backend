package com.cbm.billing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ForbiddenTransactionExeption extends Exception{

    public ForbiddenTransactionExeption() {
    }

    public ForbiddenTransactionExeption(String message) {
        super(message, null, true, false);
    }
}
