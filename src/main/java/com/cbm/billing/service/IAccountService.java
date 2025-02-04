package com.cbm.billing.service;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
import com.cbm.billing.dto.update.UpdateBillCycleDTO;
import com.cbm.billing.dto.update.UpdateBillCycleResponse;
import com.cbm.billing.dto.update.WithdrawAccountDTO;
import com.cbm.billing.dto.update.WithdrawAccountResponse;
import com.cbm.billing.exception.AccountNotFoundException;
import com.cbm.billing.exception.ForbiddenTransactionExeption;

public interface IAccountService {
    public CreateAccountResponse createAccount(CreateAccountDTO createAccountDTO);

    public UpdateBillCycleResponse updateBillCycle(Long accountId, UpdateBillCycleDTO updateBillCycleDTO) throws AccountNotFoundException;

    public WithdrawAccountResponse withdrawOnAccount(Long accountId, WithdrawAccountDTO amount) throws AccountNotFoundException, ForbiddenTransactionExeption;
}
