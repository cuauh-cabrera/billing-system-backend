package com.cbm.billing.service;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
import com.cbm.billing.dto.update.UpdateBillCycleDTO;
import com.cbm.billing.dto.update.UpdateBillCycleResponse;
import com.cbm.billing.exception.AccountNotFoundException;

public interface IAccountService {
    public CreateAccountResponse createAccount(CreateAccountDTO createAccountDTO);

    public UpdateBillCycleResponse updateBillCycle(Long accountId, UpdateBillCycleDTO updateBillCycleDTO) throws AccountNotFoundException;
}
