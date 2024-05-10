package com.example.bankappapplication.controller;

import com.example.bankappapplication.dto.CustomerAccountsResponse;
import com.example.bankappapplication.exception.AccountNotFoundException;
import com.example.bankappapplication.model.Account;
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
        public ResponseEntity<Account> getAccount(@PathVariable String accountId) {
            return ResponseEntity.ok(accountService.getAccount(accountId));
        }

        @PostMapping("/customer/{customerId}")
        public ResponseEntity<Account> createAccountForCustomer(@PathVariable Long customerId, @RequestBody Account account) {
            return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccountForCustomer(customerId, account));
        }

        @DeleteMapping("/{accountId}")
        public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
            accountService.deleteAccount(accountId);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/customer/{customerId}")
        public ResponseEntity<CustomerAccountsResponse> getCustomerAccounts(@PathVariable Long customerId) {
            return ResponseEntity.ok(accountService.getCustomerAccounts(customerId));
        }

        @ExceptionHandler(AccountNotFoundException.class)
        public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
}
