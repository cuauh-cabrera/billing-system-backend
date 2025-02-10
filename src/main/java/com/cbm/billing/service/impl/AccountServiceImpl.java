package com.cbm.billing.service.impl;

import com.cbm.billing.common.AccountStatus;
import com.cbm.billing.common.TransactionType;
import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
import com.cbm.billing.dto.event.TransactionDetailsEvent;
import com.cbm.billing.dto.model.AccountDTO;
import com.cbm.billing.dto.query.QueryAccountResponse;
import com.cbm.billing.dto.update.TransactionAmountDTO;
import com.cbm.billing.dto.update.TransactionResponse;
import com.cbm.billing.dto.update.UpdateBillCycleDTO;
import com.cbm.billing.dto.update.UpdateBillCycleResponse;
import com.cbm.billing.entity.AccountEntity;
import com.cbm.billing.exception.AccountDomainException;
import com.cbm.billing.exception.AccountNotFoundException;
import com.cbm.billing.exception.ForbiddenTransactionExeption;
import com.cbm.billing.mapper.IAccountDataMapper;
import com.cbm.billing.repository.AccountRepository;
import com.cbm.billing.service.IAccountService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

/**
 * Creates a new account using the provided {@link CreateAccountDTO}.
 * @param createAccountDTO the data for creating a new account
 * @return a {@link CreateAccountResponse} indicating the result of the account creation
 * @throws AccountDomainException if the account creation fails
 */
    @Override
    @Transactional
    public CreateAccountResponse createAccount(CreateAccountDTO createAccountDTO) {
        try {
            AccountEntity accountEntity = accountDataMapper.createAccountDTOInToAccountEntity(createAccountDTO);
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
     * function to update the bill cycle day for the account with the given id using the provided {@link UpdateBillCycleDTO}.
     *
     * @param accountId the id of the account to update
     * @param updateBillCycleDTO the data for the updated account
     * @return a {@link UpdateBillCycleResponse} containing the updated account, or an error
     *     response if the account could not be updated
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

    /**
     * function to charge the given amount from the account with the given id.
     *
     * @param accountId the id of the account to withdraw from
     * @param amount the amount to withdraw
     * @return a {@link TransactionResponse} containing the updated account, or an error
     *     response if the account could not be updated
     */
    @Override
    @Transactional
    public TransactionResponse chargeOnAccount(Long accountId, TransactionAmountDTO amount) throws AccountNotFoundException, ForbiddenTransactionExeption {
        log.info("Searching for account with id {}", accountId);

        Optional<AccountEntity> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty() || accountOptional.get().getStatus() == AccountStatus.TERMINATED) {
            log.error("Account not found with id {}", accountId);
            throw new AccountNotFoundException("Account not found with id " + accountId);
        }

        if (accountOptional.get().getCurrentBalance() < amount.getAmount()) {
            log.error("Current balance is less than amount to charge");
            throw new ForbiddenTransactionExeption("Current balance is less than amount to charge");
        }

        try {
            AccountEntity accountEntity = accountOptional.get();
            accountEntity.setCurrentBalance(accountEntity.getCurrentBalance() - amount.getAmount());
            accountRepository.save(accountEntity);
            log.info("Account with id {} updated successfully", accountEntity.getId());

            TransactionDetailsEvent transactionDetailsEvent = TransactionDetailsEvent.builder()
                    .accountId(accountEntity.getId())
                    .transactionType(TransactionType.CHARGE)
                    .previousBalance(accountEntity.getCurrentBalance() + amount.getAmount())
                    .transactionAmount(amount.getAmount())
                    .currentBalance(accountEntity.getCurrentBalance())
                    .transactionDate(LocalDate.now())
                    .build();

            return TransactionResponse.builder()
                    .code(200L)
                    .message("Charge successful")
                    .details(transactionDetailsEvent)
                    .build();

        } catch (Exception e) {
            log.error("Charge failed for account with id {}", accountId);
            throw new AccountDomainException("Charge failed for account with id " + accountId);
        }
    }

    /**
     * function to credit the given amount to the account with the given id.
     * @param accountId the id of the account to credit
     * @param amount the amount to credit
     * @return a {@link TransactionResponse} containing the updated account, or an error
     *     response if the account could not be updated
     * @throws AccountNotFoundException if the account with the given id does not exist
     * @throws ForbiddenTransactionExeption if the account is terminated
     */
    @Override
    @Transactional
    public TransactionResponse creditOnAccount(Long accountId, TransactionAmountDTO amount) throws AccountNotFoundException, ForbiddenTransactionExeption {
        log.info("Searching for account with id {}", accountId);

        Optional<AccountEntity> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty() || accountOptional.get().getStatus() == AccountStatus.TERMINATED) {
            log.error("Account not found with id {}", accountId);
            throw new AccountNotFoundException("Account not found with id " + accountId);
        }

        try {
            AccountEntity accountEntity = accountOptional.get();
            accountEntity.setCurrentBalance(accountEntity.getCurrentBalance() + amount.getAmount());
            accountRepository.save(accountEntity);
            log.info("Account with id {} updated successfully", accountEntity.getId());

            TransactionDetailsEvent transactionDetailsEvent = TransactionDetailsEvent.builder()
                    .accountId(accountEntity.getId())
                    .transactionType(TransactionType.CREDIT)
                    .previousBalance(accountEntity.getCurrentBalance() - amount.getAmount())
                    .transactionAmount(amount.getAmount())
                    .currentBalance(accountEntity.getCurrentBalance())
                    .transactionDate(LocalDate.now())
                    .build();

            return TransactionResponse.builder()
                    .code(200L)
                    .message("Credit successful")
                    .details(transactionDetailsEvent)
                    .build();

        } catch (Exception e) {
            log.error("Credit failed for account with id {}", accountId);
            throw new AccountDomainException("Credit failed for account with id " + accountId);
        }
    }

    @Override
    public QueryAccountResponse findAccountById(Long accountId) throws AccountNotFoundException {
        log.info("Searching account with id {}", accountId);

        Optional<AccountEntity> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty() || accountOptional.get().getStatus() == AccountStatus.TERMINATED) {
            log.error("Account not found with id {}", accountId);
            throw new AccountNotFoundException("Account not found with id " + accountId);
        }

        try {
            AccountEntity accountEntity = accountOptional.get();
            AccountDTO accountDTO = accountDataMapper.accountEntityToAccountDTO(accountEntity);
            log.info("Account found  with id {}", accountId);

            return QueryAccountResponse.builder()
                    .code(200L)
                    .message("Account found")
                    .accounts(List.of(accountDTO))
                    .build();

        } catch (Exception e) {
            log.error("Error finding account with id {}", accountId);
            throw new AccountDomainException("Error finding account with id " + accountId);
        }
    }
}
