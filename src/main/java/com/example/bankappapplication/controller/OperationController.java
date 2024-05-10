package com.example.bankappapplication.controller;

import com.example.bankappapplication.exception.InsufficientFundsException;
import com.example.bankappapplication.model.Operation;
import com.example.bankappapplication.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operations")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<Operation> deposit(@PathVariable String accountId, @RequestBody Map<String, Double> requestBody) {
        double amount = requestBody.get("amount");
        return ResponseEntity.ok(operationService.deposit(accountId, amount));
    }

    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<Operation> withdraw(@PathVariable String accountId, @RequestBody Map<String, Double> requestBody) {
        double amount = requestBody.get("amount");
        return ResponseEntity.ok(operationService.withdraw(accountId, amount));
    }

    @PostMapping("/transfer/{fromAccountId}/{toAccountId}")
    public ResponseEntity<Operation> transfer(@PathVariable String fromAccountId, @PathVariable String toAccountId, @RequestBody Map<String, Double> requestBody) {
        double amount = requestBody.get("amount");
        return ResponseEntity.ok(operationService.transfer(fromAccountId, toAccountId, amount));
    }

    @GetMapping("/history/account/{accountId}")
    public ResponseEntity<Iterable<Operation>> getAccountOperations(@PathVariable String accountId) {
        return ResponseEntity.ok(operationService.getAccountOperations(accountId));
    }

    @GetMapping("/history/customer/{customerId}")
    public ResponseEntity<List<Operation>> getCustomerOperations(@PathVariable Long customerId) {
        return ResponseEntity.ok(operationService.getCustomerOperations(customerId));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
