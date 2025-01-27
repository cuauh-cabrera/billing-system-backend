package com.cbm.billing.service.impl;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
import com.cbm.billing.entity.AccountEntity;
import com.cbm.billing.exception.AccountDomainException;
import com.cbm.billing.mapper.IAccountDataMapper;
import com.cbm.billing.repository.AccountRepository;
import com.cbm.billing.service.IAccountService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements IAccountService {
    private final AccountRepository accountRepository;
    private final IAccountDataMapper accountDataMapper;

    public AccountServiceImpl(AccountRepository accountRepository, IAccountDataMapper accountDataMapper) {
        this.accountRepository = accountRepository;
        this.accountDataMapper = accountDataMapper;
    }

    @Override
    @Transactional
    public CreateAccountResponse createAccount(CreateAccountDTO createAccountDTO) {
        try {
            AccountEntity accountEntity = accountDataMapper.CreateAccountDTOInToAccountEntity(createAccountDTO);
            accountRepository.save(accountEntity);
            return CreateAccountResponse.builder()
                    .code(200L)
                    .message("Account created successfully")
                    .build();
        } catch (Exception e) {
            log.error("Account creation failed");
            throw new AccountDomainException("Account creation failed");
        }
    }
}
