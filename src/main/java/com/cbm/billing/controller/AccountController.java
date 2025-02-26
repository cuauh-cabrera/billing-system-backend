package com.cbm.billing.controller;

import com.cbm.billing.common.AccountConstants;
import com.cbm.billing.common.AccountStatus;
import com.cbm.billing.common.TransactionType;
import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
import com.cbm.billing.dto.query.QueryAccountResponse;
import com.cbm.billing.dto.query.SearchAccountDTO;
import com.cbm.billing.dto.query.SearchAccountResponse;
import com.cbm.billing.dto.update.*;
import com.cbm.billing.exception.AccountNotFoundException;
import com.cbm.billing.exception.ForbiddenOperationException;
import com.cbm.billing.exception.ForbiddenTransactionExeption;
import com.cbm.billing.service.IAccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin("*")
public class AccountController {
    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }
    /**
     * Create a new account with the given {@link CreateAccountDTO}.
     * @param createAccountDTO the data for the new account
     * @return a {@link ResponseEntity} containing the created account, or an error response if the account could not be
     *     created
     */
    @PostMapping("/create")
    public ResponseEntity<CreateAccountResponse> create(@RequestBody @Valid CreateAccountDTO createAccountDTO) {
        CreateAccountResponse createAccountResponse = accountService.createAccount(createAccountDTO);
        return ResponseEntity.ok(createAccountResponse);
    }

    /**
     * Updates the bill cycle for the account with the given ID using the provided {@link UpdateBillCycleDTO}.
     * @param accountId the ID of the account to update
     * @param updateBillCycleDTO the data for the updated account
     * @return a {@link ResponseEntity} containing the updated account, or an error
     *     response if the account could not be updated
     *     @throws AccountNotFoundException if the account with the given ID does not exist
     */
    @PutMapping("/update/{accountId}")
    public ResponseEntity<UpdateBillCycleResponse> updateBillCycle(@PathVariable Long accountId,
                                                                   @RequestBody @Valid UpdateBillCycleDTO updateBillCycleDTO)
            throws AccountNotFoundException {

        UpdateBillCycleResponse updateBillCycleResponse = accountService.updateBillCycle(accountId, updateBillCycleDTO);
        return ResponseEntity.ok(updateBillCycleResponse);
    }

    /**
     * Performs a transaction on the given account.
     * @param accountId the id of the account to perform the transaction on
     * @param operation the type of transaction to perform. Should be either "charge" or "credit"
     * @param amount the amount of the transaction
     * @return a {@link ResponseEntity} containing the result of the transaction, or an error response if the
     *     transaction could not be performed
     * @throws AccountNotFoundException if the account with the given ID does not exist
     * @throws ForbiddenTransactionExeption if the operation is "charge" and the account does not have enough balance
     */
    @PutMapping("transactions/{accountId}")
    public ResponseEntity<TransactionResponse> accountTransaction(@PathVariable Long accountId,
                                                               @RequestParam String operation,
                                                               @RequestBody @Valid TransactionAmountDTO amount)
            throws AccountNotFoundException, ForbiddenTransactionExeption {

        if (operation.equals(TransactionType.CHARGE.name().toLowerCase())) {
            TransactionResponse chargeAccountResponse = accountService.chargeOnAccount(accountId, amount);
            return ResponseEntity.ok(chargeAccountResponse);
        }

        TransactionResponse creditAccountResponse = accountService.creditOnAccount(accountId, amount);
        return ResponseEntity.ok(creditAccountResponse);
    }

    /**
     * Retrieves an account by ID.
     * @param accountId the ID of the account to retrieve
     * @return a {@link ResponseEntity} containing the retrieved account, or an error
     *     response if the account could not be found
     * @throws AccountNotFoundException if the account with the given ID does not exist
     */
    @GetMapping("{accountId}")
    public ResponseEntity<QueryAccountResponse> findAccount(@PathVariable Long accountId) throws AccountNotFoundException {
        QueryAccountResponse queryAccountResponse = accountService.findAccountById(accountId);
        return ResponseEntity.ok(queryAccountResponse);
    }

    /**
     * Retrieves a list of accounts that match the given criteria.
     * @param page the page number of the list to retrieve, starting from 0
     * @param size the number of accounts to retrieve per page
     * @param sort the field to sort the accounts by. Can be "name", "bill_cycle_day",
     *     or "last_bill_date". The default is "name"
     * @param bill_cicle the bill cycle day to filter by
     * @param last_bill the last bill date to filter by
     * @param status the status of the accounts to filter by
     * @param name the name of the accounts to filter by
     * @return a {@link ResponseEntity} containing the retrieved accounts, or an error
     *     response if no accounts match the criteria
     */
    @GetMapping("/search")
    public ResponseEntity<SearchAccountResponse> searchAccount(@RequestParam(defaultValue = AccountConstants.DEFAULT_PAGE, required = false) int page,
                                                               @RequestParam(defaultValue = AccountConstants.PAGE_SIZE, required = false) int size,
                                                               @RequestParam(required = false) String sort,
                                                               @RequestParam(required = false) Integer bill_cicle,
                                                               @RequestParam(required = false) LocalDate last_bill,
                                                               @RequestParam(required = false) AccountStatus status,
                                                               @RequestParam(required = false) String name) {

        SearchAccountDTO searchAccountDTO = SearchAccountDTO.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .billCycleDay(bill_cicle)
                .lastBillDate(last_bill)
                .status(status)
                .name(name)
                .build();

        SearchAccountResponse searchAccountResponse = accountService.searchAccount(page, size, sort,searchAccountDTO);
        return ResponseEntity.ok(searchAccountResponse);

    }

    /**
     * Terminates the account with the given ID.
     * @param accountId the ID of the account to terminate
     * @param updateAccountStatusDTO the data for the terminated account
     * @return a {@link ResponseEntity} containing the terminated account, or an error response if the account could not be terminated
     * @throws AccountNotFoundException if the account with the given ID does not exist
     * @throws ForbiddenOperationException if the account with the given ID is not active
     */
    @PutMapping("/inactivate/{accountId}")
    public ResponseEntity<UpdateAccountStatusResponse> inactivateAccount(@PathVariable Long accountId,
                                                                         @RequestBody @Valid UpdateAccountStatusDTO updateAccountStatusDTO)
            throws AccountNotFoundException, ForbiddenOperationException {

        UpdateAccountStatusResponse updateAccountStatusResponse = accountService.terminateAccount(accountId, updateAccountStatusDTO);
        return ResponseEntity.ok(updateAccountStatusResponse);
    }
}
