package com.example.bankappapplication.service;

import com.example.bankappapplication.exception.InsufficientFundsException;
import com.example.bankappapplication.model.OperationType;
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

@Service
public class OperationService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Operation deposit(String accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setOperationDate(new Date());
        operation.setType(OperationType.DEPOT);
        operation.setAmount(amount);
        return operationRepository.save(operation);
    }

    @Transactional
    public Operation withdraw(String accountId, double amount) {
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
        return operationRepository.save(operation);
    }

    @Transactional
    public Operation transfer(String fromAccountId, String toAccountId, double amount) {
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
        return operationRepository.save(operation);
    }

    public List<Operation> getAccountOperations(String accountId) {
        return operationRepository.findAllByAccountId(accountId);
    }

    public List<Operation> getCustomerOperations(Long customerId) {
        List<Account> accounts = accountRepository.findAllByCustomerId(customerId);
        List<Operation> operations = new ArrayList<>();
        for (Account account : accounts) {
            operations.addAll(operationRepository.findAllByAccountId(account.getId()));
        }
        return operations;
    }

}
