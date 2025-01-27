package com.cbm.billing.service;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;

public interface IAccountService {
    public CreateAccountResponse createAccount(CreateAccountDTO createAccountDTO);
}
