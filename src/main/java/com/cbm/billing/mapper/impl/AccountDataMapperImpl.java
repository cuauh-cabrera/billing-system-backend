package com.cbm.billing.mapper.impl;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.query.SearchAccountDTO;
import com.cbm.billing.entity.AccountEntity;
import com.cbm.billing.mapper.IAccountDataMapper;
import com.cbm.billing.model.Account;
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
     * Converts an AccountEntity to an Account.
     * @param accountEntity the AccountEntity to convert
     * @return the converted Account
     */
    @Override
    public Account accountEntityToAccount(AccountEntity accountEntity) {
        return new Account(
                accountEntity.getId(), // id
                accountEntity.getName(), // name
                accountEntity.getCurrentBalance(), // currentBalance
                accountEntity.getBillCycleDay(), // billCycleDay
                accountEntity.getLastBillDate(), // lastBillDate
                accountEntity.getStatus(), // status
                null, // createdAt
                null // updatedAt
        );
    }

    /**
     * Converts a SearchAccountDTO to an Account.
     * @param searchAccountDTO the SearchAccountDTO to convert
     * @return the converted Account with fields populated from the SearchAccountDTO
     */
    @Override
    public Account searchAccountDTOToAccount(SearchAccountDTO searchAccountDTO) {
        return new Account(
                null, // id
                searchAccountDTO.getName(), // name
                null, // currentBalance
                searchAccountDTO.getBillCycleDay(), // billCycleDay
                searchAccountDTO.getLastBillDate(), // lastBillDate
                searchAccountDTO.getStatus(), // status
                null, // createdAt
                null // updatedAt
        );
    }
}
