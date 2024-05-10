package com.example.bankappapplication.service;

import com.example.bankappapplication.dto.CustomerAccountsResponseDTO;
import com.example.bankappapplication.dto.AccountDTO;
import com.example.bankappapplication.dto.CustomerDTO;
import com.example.bankappapplication.exception.AccountNotFoundException;
import com.example.bankappapplication.exception.CustomerNotFoundException;
import com.example.bankappapplication.mapper.AccountMapper;
import com.example.bankappapplication.mapper.CustomerMapper;
import com.example.bankappapplication.model.Account;
import com.example.bankappapplication.model.Customer;
import com.example.bankappapplication.repository.AccountRepository;
import com.example.bankappapplication.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CustomerMapper customerMapper;

    public CustomerAccountsResponseDTO getCustomerAccounts(Long customerId) {
        Customer customer = customerRepository.findCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        List<Account> accounts = accountRepository.findAllByCustomerId(customerId);
        List<AccountDTO> accountDTOs = accounts.stream()
                .map(accountMapper::mapToAccountDTO)
                .collect(Collectors.toList());
        CustomerDTO customerDTO = customerMapper.mapToCustomerDTO(customer);
        return new CustomerAccountsResponseDTO(customerDTO, accountDTOs);
    }

    public AccountDTO createAccountForCustomer(Long customerId, AccountDTO accountDTO) {
        Customer customer = customerRepository.findCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        Account account = accountMapper.mapToAccount(accountDTO);
        account.setCustomer(customer);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.mapToAccountDTO(savedAccount);
    }

    public AccountDTO getAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));
        return accountMapper.mapToAccountDTO(account);
    }

    public void deleteAccount(String accountId) {
        accountRepository.deleteById(accountId);
    }
}
