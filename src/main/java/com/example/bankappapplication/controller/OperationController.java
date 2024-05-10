package com.example.bankappapplication.controller;

import com.example.bankappapplication.dto.OperationDTO;
import com.example.bankappapplication.exception.InsufficientFundsException;
import com.example.bankappapplication.mapper.OperationMapper;
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

    @Autowired
    private OperationMapper operationMapper;

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<OperationDTO> deposit(@PathVariable String accountId, @RequestBody Map<String, Double> requestBody) {
        double amount = requestBody.get("amount");
        OperationDTO operationDTO = operationService.deposit(accountId, amount);
        return ResponseEntity.ok(operationDTO);
    }

    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<OperationDTO> withdraw(@PathVariable String accountId, @RequestBody Map<String, Double> requestBody) {
        double amount = requestBody.get("amount");
        OperationDTO operationDTO = operationService.withdraw(accountId, amount);
        return ResponseEntity.ok(operationDTO);
    }

    @PostMapping("/transfer/{fromAccountId}/{toAccountId}")
    public ResponseEntity<OperationDTO> transfer(@PathVariable String fromAccountId, @PathVariable String toAccountId, @RequestBody Map<String, Double> requestBody) {
        double amount = requestBody.get("amount");
        OperationDTO operationDTO = operationService.transfer(fromAccountId, toAccountId, amount);
        return ResponseEntity.ok(operationDTO);
    }

    @GetMapping("/history/account/{accountId}")
    public ResponseEntity<List<OperationDTO>> getAccountOperations(@PathVariable String accountId) {
        List<OperationDTO> operationDTOs = operationService.getAccountOperations(accountId);
        return ResponseEntity.ok(operationDTOs);
    }

    @GetMapping("/history/customer/{customerId}")
    public ResponseEntity<List<OperationDTO>> getCustomerOperations(@PathVariable Long customerId) {
        List<OperationDTO> operationDTOs = operationService.getCustomerOperations(customerId);
        return ResponseEntity.ok(operationDTOs);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
