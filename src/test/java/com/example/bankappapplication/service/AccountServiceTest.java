package com.example.bankappapplication.service;

import com.example.bankappapplication.dto.CustomerAccountsResponse;
import com.example.bankappapplication.model.Account;
import com.example.bankappapplication.model.Customer;
import com.example.bankappapplication.repository.AccountRepository;
import com.example.bankappapplication.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testGetCustomerAccounts() {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Nabil");
        customer.setEmail("nabil@example.com");
        Account account1 = new Account("123", 1000.0, 0.0, new Date(), customer, null);
        Account account2 = new Account("456", 2000.0, 0.0, new Date(), customer, null);
        List<Account> accounts = Arrays.asList(account1, account2);

        when(customerRepository.findCustomerById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.findAllByCustomerId(customerId)).thenReturn(accounts);

        // Act
        CustomerAccountsResponse response = accountService.getCustomerAccounts(customerId);

        // Assert
        assertEquals(customer, response.getCustomer());
        assertEquals(accounts, response.getAccounts());
    }

    @Test
    public void testCreateAccountForCustomer() {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Nabil");
        customer.setEmail("nabil@example.com");
        Account account = new Account("123", 1000.0, 0.0, new Date(), customer, null);
        when(customerRepository.findCustomerById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.save(account)).thenReturn(account);

        // Act
        Account createdAccount = accountService.createAccountForCustomer(customerId, account);

        // Assert
        assertEquals(account, createdAccount);
    }

    @Test
    public void testGetAccount() {
        // Arrange
        String accountId = "123";
        Account account = new Account(accountId, 1000.0, 0.0, new Date(), null, null);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        Account retrievedAccount = accountService.getAccount(accountId);

        // Assert
        assertEquals(account, retrievedAccount);
    }

    @Test
    public void testDeleteAccount() {
        // Arrange
        String accountId = "123";

        // Act
        accountService.deleteAccount(accountId);

        // Assert
        verify(accountRepository, times(1)).deleteById(accountId);
    }
}