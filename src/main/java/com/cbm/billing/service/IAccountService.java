package com.cbm.billing.service;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
import com.cbm.billing.dto.query.QueryAccountResponse;
import com.cbm.billing.dto.query.SearchAccountDTO;
import com.cbm.billing.dto.query.SearchAccountResponse;
import com.cbm.billing.dto.update.*;
import com.cbm.billing.exception.AccountNotFoundException;
import com.cbm.billing.exception.ForbiddenOperationException;
import com.cbm.billing.exception.ForbiddenTransactionExeption;

public interface IAccountService {

    public CreateAccountResponse createAccount(CreateAccountDTO createAccountDTO);

    public UpdateBillCycleResponse updateBillCycle(Long accountId, UpdateBillCycleDTO updateBillCycleDTO) throws AccountNotFoundException;

    public TransactionResponse chargeOnAccount(Long accountId, TransactionAmountDTO amount) throws AccountNotFoundException, ForbiddenTransactionExeption;

    public TransactionResponse creditOnAccount(Long accountId, TransactionAmountDTO amount) throws AccountNotFoundException;

    public QueryAccountResponse findAccountById(Long accountId) throws AccountNotFoundException;

    public SearchAccountResponse searchAccount(int page, int size, String sort, SearchAccountDTO filters);

    public UpdateAccountStatusResponse terminateAccount(Long accountId, UpdateAccountStatusDTO updateAccountStatusDTO) throws AccountNotFoundException, ForbiddenOperationException;
}
