package com.cbm.billing.mapper;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.model.AccountDTO;
import com.cbm.billing.entity.AccountEntity;

public interface IAccountDataMapper {
    public AccountEntity createAccountDTOInToAccountEntity(CreateAccountDTO createAccountDTO);

    public AccountDTO accountEntityToAccountDTO(AccountEntity accountEntity);

}
