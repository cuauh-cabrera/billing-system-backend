package com.cbm.billing.controller;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
import com.cbm.billing.dto.update.UpdateBillCycleDTO;
import com.cbm.billing.dto.update.UpdateBillCycleResponse;
import com.cbm.billing.dto.update.WithdrawAccountDTO;
import com.cbm.billing.dto.update.WithdrawAccountResponse;
import com.cbm.billing.exception.AccountNotFoundException;
import com.cbm.billing.exception.ForbiddenTransactionExeption;
import com.cbm.billing.service.IAccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     *
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
     * Updates the bill cycle for the account with the given ID using the provided
     * {@link UpdateBillCycleDTO}.
     *
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
     * Withdraws the given amount from the account with the given ID.
     *
     * @param accountId the ID of the account to withdraw from
     * @param amount the amount to withdraw
     * @return a {@link ResponseEntity} containing the updated account, or an error response if the account could not be
     *     updated
     * @throws AccountNotFoundException if the account with the given ID does not exist
     * @throws ForbiddenTransactionExeption if the account is terminated or the current balance is insufficient
     */
    @PutMapping("transactions/withdraw/{accountId}")
    public ResponseEntity<WithdrawAccountResponse> withdrawOnAccount(@PathVariable Long accountId,
                                                                     @RequestBody @Valid WithdrawAccountDTO amount)
            throws AccountNotFoundException, ForbiddenTransactionExeption {

        WithdrawAccountResponse withdrawAccountResponse = accountService.withdrawOnAccount(accountId, amount);
        return ResponseEntity.ok(withdrawAccountResponse);
    }
}
