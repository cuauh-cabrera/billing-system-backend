package com.cbm.billing.mapper;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.query.SearchAccountDTO;
import com.cbm.billing.model.Account;
import com.cbm.billing.entity.AccountEntity;

public interface IAccountDataMapper {
    public AccountEntity createAccountDTOInToAccountEntity(CreateAccountDTO createAccountDTO);

    public Account accountEntityToAccount(AccountEntity accountEntity);

    public Account searchAccountDTOToAccount(SearchAccountDTO searchAccountDTO);

}
