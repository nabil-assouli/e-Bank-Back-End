package com.example.bankappapplication.service;

import com.example.bankappapplication.exception.InsufficientFundsException;
import com.example.bankappapplication.mapper.OperationMapper;
import com.example.bankappapplication.model.OperationType;
import com.example.bankappapplication.dto.OperationDTO;
import com.example.bankappapplication.exception.AccountNotFoundException;
import com.example.bankappapplication.model.Account;
import com.example.bankappapplication.model.Operation;
import com.example.bankappapplication.repository.AccountRepository;
import com.example.bankappapplication.repository.OperationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationService {
    @Autowired
    private AccountService accountService;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationMapper operationMapper;

    @Transactional
    public OperationDTO deposit(String accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setOperationDate(new Date());
        operation.setType(OperationType.DEPOT);
        operation.setAmount(amount);
        Operation savedOperation = operationRepository.save(operation);
        return operationMapper.mapToOperationDTO(savedOperation);
    }

    @Transactional
    public OperationDTO withdraw(String accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        if (account.getBalance() + account.getOverDraft() < amount) {
            throw new InsufficientFundsException(accountId);
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setOperationDate(new Date());
        operation.setType(OperationType.RETRAIT);
        operation.setAmount(amount);
        Operation savedOperation = operationRepository.save(operation);
        return operationMapper.mapToOperationDTO(savedOperation);
    }

    @Transactional
    public OperationDTO transfer(String fromAccountId, String toAccountId, double amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        if (!fromAccount.getCustomer().getId().equals(toAccount.getCustomer().getId())) {
            throw new IllegalArgumentException("Both accounts must belong to the same customer");
        }

        if (fromAccount.getBalance() + fromAccount.getOverDraft() < amount) {
            throw new InsufficientFundsException(fromAccountId);
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Operation operation = new Operation();
        operation.setAccount(fromAccount);
        operation.setOperationDate(new Date());
        operation.setType(OperationType.VIREMENT);
        operation.setAmount(amount);
        Operation savedOperation = operationRepository.save(operation);
        return operationMapper.mapToOperationDTO(savedOperation);
    }

    public List<OperationDTO> getAccountOperations(String accountId) {
        List<Operation> operations = operationRepository.findAllByAccountId(accountId);
        return operations.stream()
                .map(operationMapper::mapToOperationDTO)
                .collect(Collectors.toList());
    }

    public List<OperationDTO> getCustomerOperations(Long customerId) {
        List<Account> accounts = accountRepository.findAllByCustomerId(customerId);
        List<OperationDTO> operations = new ArrayList<>();
        for (Account account : accounts) {
            List<Operation> accountOperations = operationRepository.findAllByAccountId(account.getId());
            operations.addAll(accountOperations.stream()
                    .map(operationMapper::mapToOperationDTO)
                    .collect(Collectors.toList()));
        }
        return operations;
    }
}

