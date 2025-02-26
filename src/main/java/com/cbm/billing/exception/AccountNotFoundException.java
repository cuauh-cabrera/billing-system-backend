package com.cbm.billing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends Exception {

    public void AccountDomainException(){}

    public AccountNotFoundException(String message) {
        super(message, null, true, false);
    }

}
