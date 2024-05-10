package com.example.bankappapplication.controller;

import com.example.bankappapplication.dto.AccountDTO;
import com.example.bankappapplication.dto.CustomerAccountsResponseDTO;
import com.example.bankappapplication.exception.AccountNotFoundException;
import com.example.bankappapplication.mapper.AccountMapper;
import com.example.bankappapplication.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String accountId) {
        AccountDTO accountDTO = accountService.getAccount(accountId);
        return ResponseEntity.ok(accountDTO);
    }

    @PostMapping("/customer/{customerId}")
    public ResponseEntity<AccountDTO> createAccountForCustomer(@PathVariable Long customerId, @RequestBody AccountDTO accountDTO) {
        AccountDTO savedAccountDTO = accountService.createAccountForCustomer(customerId, accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccountDTO);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CustomerAccountsResponseDTO> getCustomerAccounts(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getCustomerAccounts(customerId));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}

