package com.cbm.billing.service.impl;

import com.cbm.billing.common.AccountStatus;
import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
import com.cbm.billing.dto.update.UpdateBillCycleDTO;
import com.cbm.billing.dto.update.UpdateBillCycleResponse;
import com.cbm.billing.entity.AccountEntity;
import com.cbm.billing.exception.AccountDomainException;
import com.cbm.billing.exception.AccountNotFoundException;
import com.cbm.billing.mapper.IAccountDataMapper;
import com.cbm.billing.repository.AccountRepository;
import com.cbm.billing.service.IAccountService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    /**
     * Updates the bill cycle day for the account with the given id using the provided {@link UpdateBillCycleDTO}.
     *
     * @param accountId the id of the account to update
     * @param updateBillCycleDTO the data for the updated account
     * @return a {@link UpdateBillCycleResponse} containing the updated account, or an error
     *     response if the account could not be updated
     * @throws AccountNotFoundException if no account is found with the given id
     */
    @Override
    @Transactional
    public UpdateBillCycleResponse updateBillCycle(Long accountId, UpdateBillCycleDTO updateBillCycleDTO) throws AccountNotFoundException {
        log.info("Searching for account with id {}", accountId);

        Optional<AccountEntity> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty() || accountOptional.get().getStatus() == AccountStatus.TERMINATED) {
            log.error("Account not found");
            throw  new AccountNotFoundException("Account not found with id " + accountId);
        }

        log.info("Found account with id {}", accountId);

        try {
            AccountEntity accountEntity = accountOptional.get();
            accountEntity.setBillCycleDay(updateBillCycleDTO.getBillCycleDay());
            accountRepository.save(accountEntity);
            log.info("Account with id {} updated successfully", accountEntity.getId());

            return UpdateBillCycleResponse.builder()
                    .code(200L)
                    .accountId(accountId)
                    .message("Bill cycle updated successfully")
                    .build();

        } catch (Exception e) {
            log.error("Bill cycle update failed");
            throw new AccountDomainException("Bill cycle update failed");
        }
    }
}
