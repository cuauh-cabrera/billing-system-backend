package com.cbm.billing.service.impl;

import com.cbm.billing.common.AccountStatus;
import com.cbm.billing.dto.create.CreateBillDTO;
import com.cbm.billing.dto.create.CreateBillEvent;
import com.cbm.billing.dto.create.CreateBillResponse;
import com.cbm.billing.entity.AccountEntity;
import com.cbm.billing.entity.BillEntity;
import com.cbm.billing.entity.BillSummaryEntity;
import com.cbm.billing.exception.AccountNotFoundException;
import com.cbm.billing.exception.BillDomainException;
import com.cbm.billing.mapper.IBillDataMapper;
import com.cbm.billing.repository.AccountRepository;
import com.cbm.billing.repository.BillRepository;
import com.cbm.billing.repository.BillSummaryRepository;
import com.cbm.billing.service.IBillService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class BillServiceImpl implements IBillService {
    private final BillRepository billRepository;
    private final AccountRepository accountRepository;
    private final BillSummaryRepository billSummaryRepository;
    private final IBillDataMapper billDataMapper;

    public BillServiceImpl(BillRepository billRepository, AccountRepository accountRepository, BillSummaryRepository billSummaryRepository, IBillDataMapper billDataMapper) {
        this.billRepository = billRepository;
        this.accountRepository = accountRepository;
        this.billSummaryRepository = billSummaryRepository;
        this.billDataMapper = billDataMapper;
    }

/**
 * Creates a new bill for the specified account.
 * This method retrieves the current balance of the account, creates a new bill
 * with the specified amount, updates the account's current balance by adding the
 * bill amount, and returns a response containing details of the bill creation event.
 * @param createBillDTO the data transfer object containing the account ID and bill amount
 * @return a {@link CreateBillResponse} containing the details of the created bill
 * @throws AccountNotFoundException if the account with the specified ID is not found or is terminated
 */
    @Override
    @Transactional
    public CreateBillResponse createBill(CreateBillDTO createBillDTO) throws AccountNotFoundException {
        log.info("Retrieving current balance for account with id {}", createBillDTO.getAccountId());

        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(createBillDTO.getAccountId());

        if (accountEntityOptional.isEmpty() || accountEntityOptional.get().getStatus() == AccountStatus.TERMINATED) {
            log.error("Account not found with id {}", createBillDTO.getAccountId());
            throw new AccountNotFoundException("Account not found with id " + createBillDTO.getAccountId());
        }

        try {
            // Get the current balance for the account
            AccountEntity accountEntity = accountEntityOptional.get();
            var accountCurrentBalance = accountEntity.getCurrentBalance();
            log.info("Current balance for account with id {} is $ {}", createBillDTO.getAccountId(), accountCurrentBalance);

            // Create the bill
            var billAmount = createBillDTO.getAmount();
            createBillDTO.setAmount(accountCurrentBalance + billAmount);
            BillEntity billEntity = billDataMapper.CreateBillDTOInToBillEntity(createBillDTO);
            billRepository.save(billEntity);
            log.info("Bill created successfully for account with id {} and amount $ {}", createBillDTO.getAccountId(),billAmount);

            // Propagate the new bill to the bill summary table
            BillSummaryEntity billSummary = new BillSummaryEntity();
            billSummary.setId(billEntity.getId());
            billSummary.setStatus(billEntity.getStatus());
            billSummaryRepository.save(billSummary);
            log.info("Bill propagated to bill summary successfully for account with id {} and amount $ {}", createBillDTO.getAccountId(),billAmount);

            // Update the account current balance
            accountEntity.setCurrentBalance(accountCurrentBalance + billAmount);
            accountEntity.setLastBillDate(LocalDate.now());
            accountRepository.save(accountEntity);
            log.info("Account with id {} updated successfully", createBillDTO.getAccountId());

            CreateBillEvent createBillEvent = CreateBillEvent.builder()
                    .accountId(createBillDTO.getAccountId())
                    .currentBalance(accountCurrentBalance)
                    .billAmount(billAmount)
                    .newBalance(accountCurrentBalance + billAmount)
                    .billGenerationDate(LocalDate.now())
                    .build();

            return CreateBillResponse.builder()
                    .code(200L)
                    .message("Bill created successfully")
                    .details(createBillEvent)
                    .build();

        } catch (Exception e) {
            log.error("Error creating bill for account with id {}", createBillDTO.getAccountId(), e);
            throw new BillDomainException("Error creating bill for account with id " + createBillDTO.getAccountId());
        }
    }
}
