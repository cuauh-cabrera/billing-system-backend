package com.cbm.billing.service.impl;

import com.cbm.billing.common.AccountStatus;
import com.cbm.billing.common.TransactionType;
import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
import com.cbm.billing.dto.event.TransactionDetailsEvent;
import com.cbm.billing.dto.event.UpdateAccountStatusEvent;
import com.cbm.billing.dto.query.QueryAccountResponse;
import com.cbm.billing.dto.query.SearchAccountDTO;
import com.cbm.billing.dto.query.SearchAccountResponse;
import com.cbm.billing.dto.update.*;
import com.cbm.billing.entity.AccountEntity;
import com.cbm.billing.exception.AccountDomainException;
import com.cbm.billing.exception.AccountNotFoundException;
import com.cbm.billing.exception.ForbiddenOperationException;
import com.cbm.billing.exception.ForbiddenTransactionExeption;
import com.cbm.billing.mapper.IAccountDataMapper;
import com.cbm.billing.model.Account;
import com.cbm.billing.repository.AccountRepository;
import com.cbm.billing.service.IAccountService;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AccountServiceImpl implements IAccountService {
    private final AccountRepository accountRepository;
    private final IAccountDataMapper accountDataMapper;
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public AccountServiceImpl(AccountRepository accountRepository, IAccountDataMapper accountDataMapper, EntityManager entityManager) {
        this.accountRepository = accountRepository;
        this.accountDataMapper = accountDataMapper;
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
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
    public TransactionResponse creditOnAccount(Long accountId, TransactionAmountDTO amount) throws AccountNotFoundException {
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

    /**
     * Retrieves an account by ID.
     * @param accountId the ID of the account to retrieve
     * @return a {@link QueryAccountResponse} containing the retrieved account, or an error
     *     response if the account could not be found
     * @throws AccountNotFoundException if the account with the given ID does not exist
     */
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
            Account accountDTO = accountDataMapper.accountEntityToAccount(accountEntity);
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

    /**
     * Retrieves a list of accounts that match the given search filters.
     * @param page the page of the search results to retrieve
     * @param size the number of results to include in each page
     * @param sort the field to sort the results by
     * @param filters the search filters to apply
     * @return a {@link SearchAccountResponse} containing the search results, or an error
     *     response if the search could not be performed
     * @throws AccountDomainException if the search could not be performed
     */
    @Override
    public SearchAccountResponse searchAccount(int page, int size, String sort, SearchAccountDTO filters) {
        log.info("Searching accounts");

        try {
            CriteriaQuery<AccountEntity> criteriaQuery = criteriaBuilder.createQuery(AccountEntity.class);
            Root<AccountEntity> accountEntityRoot = criteriaQuery.from(AccountEntity.class);
            Predicate predicate = searchAccountPredicate(accountDataMapper.searchAccountDTOToAccount(filters), accountEntityRoot);
            log.info("Building predicate with filters: {}", predicate);

            criteriaQuery.where(predicate);
            TypedQuery<AccountEntity> typedQuery = entityManager.createQuery(criteriaQuery);
            log.info("Executing query...");

            Sort sortCriteria = Sort.by(Sort.Direction.ASC, "name");
            Pageable pageable = PageRequest.of(page, size, sortCriteria);
            typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            typedQuery.setMaxResults(pageable.getPageSize());
            long totalCount = entityManager.createQuery(criteriaQuery).getResultList().size();

            List<AccountEntity> accountEntities = typedQuery.getResultList();
            List<Account> accounts = accountEntities.stream()
                    .map(accountDataMapper::accountEntityToAccount)
                    .toList();

            log.info("Accounts found: {}", totalCount);

            return SearchAccountResponse.builder()
                    .total(totalCount)
                    .page(page)
                    .size(size)
                    .accounts(accounts)
                    .build();

        } catch (Exception e) {
            log.error("Error searching accounts");
            throw new AccountDomainException("Error searching accounts");
        }
    }

    /**
     * Update the status of an ACTIVE account with the given id to TERMINATE.
     * @param accountId the id of the account to update (must be ACTIVE)
     * @param updateAccountStatusDTO contains the new status (must be TERMINATED)
     * @return a {@link UpdateAccountStatusResponse} containing the updated account, or an error response if the account could not be updated
     * @throws AccountNotFoundException if the account with the given id does not exist
     * @throws ForbiddenOperationException if the account is already TERMINATED
     */
    @Override
    @Transactional
    public UpdateAccountStatusResponse terminateAccount(Long accountId, UpdateAccountStatusDTO updateAccountStatusDTO) throws AccountNotFoundException, ForbiddenOperationException {
        log.info("Updating account status with id {}", accountId);

        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(accountId);

        if(accountEntityOptional.isEmpty()) {
            log.error("Account not found with id {}", accountId);
            throw new AccountNotFoundException("Account not found with id " + accountId);
        }

        if (accountEntityOptional.get().getStatus() == AccountStatus.TERMINATED) {
            log.error("Account is already terminated with id {}", accountId);
            throw new ForbiddenOperationException("Account with id " + accountId + " is already terminated. You cannot perform any modification on terminated accounts");
        }

        try {
            AccountEntity accountEntity = accountEntityOptional.get();
            accountEntity.setStatus(updateAccountStatusDTO.getStatus());
            accountRepository.save(accountEntity);
            log.info("Account with id {} updated successfully", accountEntity.getId());

            UpdateAccountStatusEvent updateAccountStatusEvent = UpdateAccountStatusEvent.builder()
                    .accountId(accountEntity.getId())
                    .updatedStatus(accountEntity.getStatus())
                    .updatedAt(LocalDate.now())
                    .build();

            return UpdateAccountStatusResponse.builder()
                    .code(200L)
                    .message("Account status updated successfully")
                    .details(updateAccountStatusEvent)
                    .build();

        }catch (Exception e) {
            log.error("Error updating account status with id {}", accountId);
            throw new AccountDomainException("Error updating account status with id " + accountId);
        }
    }

    /**
     * Auxiliary method that generates a JPA predicate from the given search filters.
     * @param filters the search filters
     * @param accountEntityRoot the root of the JPA query
     * @return a JPA predicate that can be used to filter the results of the query
     */
    private Predicate searchAccountPredicate(Account filters, Root<AccountEntity> accountEntityRoot) {
        List<Predicate> predicates = new ArrayList<>();

        if (filters.getName() != null && StringUtils.isNotEmpty(filters.getName())) {
            predicates.add(criteriaBuilder.like(accountEntityRoot.get("name"),"%" + filters.getName() + "%"));
        }

        if (filters.getStatus() != null && StringUtils.isNotEmpty(filters.getStatus().toString())) {
            predicates.add(criteriaBuilder.equal(accountEntityRoot.get("status"), filters.getStatus()));
        }

        if (filters.getBillCycleDay() != null && StringUtils.isNotEmpty(filters.getBillCycleDay().toString())) {
            predicates.add(criteriaBuilder.equal(accountEntityRoot.get("billCycleDay"), filters.getBillCycleDay()));
        }

        if (filters.getLastBillDate() != null && StringUtils.isNotEmpty(filters.getLastBillDate().toString())) {
            predicates.add(criteriaBuilder.equal(accountEntityRoot.get("lastBillDate"), filters.getLastBillDate()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
