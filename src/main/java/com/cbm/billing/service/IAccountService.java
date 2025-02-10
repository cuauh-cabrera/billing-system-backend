package com.cbm.billing.service;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
import com.cbm.billing.dto.query.QueryAccountResponse;
import com.cbm.billing.dto.update.UpdateBillCycleDTO;
import com.cbm.billing.dto.update.UpdateBillCycleResponse;
import com.cbm.billing.dto.update.TransactionAmountDTO;
import com.cbm.billing.dto.update.TransactionResponse;
import com.cbm.billing.exception.AccountNotFoundException;
import com.cbm.billing.exception.ForbiddenTransactionExeption;

public interface IAccountService {
    public CreateAccountResponse createAccount(CreateAccountDTO createAccountDTO);

    public UpdateBillCycleResponse updateBillCycle(Long accountId, UpdateBillCycleDTO updateBillCycleDTO) throws AccountNotFoundException;

    public TransactionResponse chargeOnAccount(Long accountId, TransactionAmountDTO amount) throws AccountNotFoundException, ForbiddenTransactionExeption;

    public TransactionResponse creditOnAccount(Long accountId, TransactionAmountDTO amount) throws AccountNotFoundException, ForbiddenTransactionExeption;

    public QueryAccountResponse findAccountById(Long accountId) throws AccountNotFoundException;
}
