package com.cbm.billing.controller;

import com.cbm.billing.dto.create.CreateAccountDTO;
import com.cbm.billing.dto.create.CreateAccountResponse;
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
}
