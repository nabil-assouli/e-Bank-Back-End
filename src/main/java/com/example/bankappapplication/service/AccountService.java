package com.example.bankappapplication.service;

import com.example.bankappapplication.dto.CustomerAccountsResponse;
import com.example.bankappapplication.exception.AccountNotFoundException;
import com.example.bankappapplication.exception.CustomerNotFoundException;
import com.example.bankappapplication.model.Account;
import com.example.bankappapplication.model.Customer;
import com.example.bankappapplication.repository.AccountRepository;
import com.example.bankappapplication.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

   @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerAccountsResponse getCustomerAccounts(Long customerId) {
        Customer customer = customerRepository.findCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        List<Account> accounts = accountRepository.findAllByCustomerId(customerId);
        return new CustomerAccountsResponse(customer, accounts);
    }

    public Account createAccountForCustomer(Long customerId, Account account) {
        Customer customer = customerRepository.findCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        account.setCustomer(customer);
        return accountRepository.save(account);
    }

    public Account getAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));
    }

    public void deleteAccount(String accountId) {
        accountRepository.deleteById(accountId);
    }



}