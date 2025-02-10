package com.cbm.billing.mapper.impl;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.model.AccountDTO;
import com.cbm.billing.entity.AccountEntity;
import com.cbm.billing.mapper.IAccountDataMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountDataMapperImpl implements IAccountDataMapper {

    /**
     * Maps a CreateAccountDTO to an AccountEntity.
     * @param createAccountDTO the CreateAccountDTO to map
     * @return the mapped AccountEntity
     */
    @Override
    public AccountEntity createAccountDTOInToAccountEntity(CreateAccountDTO createAccountDTO) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setName(createAccountDTO.getName());
        accountEntity.setCurrentBalance(createAccountDTO.getCurrentBalance());
        accountEntity.setBillCycleDay(createAccountDTO.getBillCycleDay());
        accountEntity.setStatus(createAccountDTO.getStatus());
        return accountEntity;
    }

    /**
     * Converts an AccountEntity to an AccountDTO.
     * @param accountEntity the AccountEntity to convert
     * @return the converted AccountDTO
     */
    @Override
    public AccountDTO accountEntityToAccountDTO(AccountEntity accountEntity) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(accountEntity.getId());
        accountDTO.setName(accountEntity.getName());
        accountDTO.setCurrentBalance(accountEntity.getCurrentBalance());
        accountDTO.setStatus(accountEntity.getStatus());
        return accountDTO;
    }
}

