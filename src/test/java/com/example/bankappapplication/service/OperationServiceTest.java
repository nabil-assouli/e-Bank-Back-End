package com.example.bankappapplication.service;

import com.example.bankappapplication.model.Account;
import com.example.bankappapplication.model.Customer;
import com.example.bankappapplication.model.Operation;
import com.example.bankappapplication.repository.AccountRepository;
import com.example.bankappapplication.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.bankappapplication.exception.InsufficientFundsException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OperationServiceTest {

    @InjectMocks
    private OperationService operationService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(operationRepository.save(any(Operation.class))).thenAnswer(invocation -> (Operation) invocation.getArguments()[0]);
    }

    @Test
    public void testDeposit() {
        Account account = new Account();
        account.setId("testId");
        account.setBalance(1000.0);

        when(accountRepository.findById("testId")).thenReturn(Optional.of(account));

        Operation operation = operationService.deposit("testId", 500.0);

        assertEquals(1500.0, account.getBalance());
        assertEquals(500.0, operation.getAmount());
    }

    @Test
    public void testWithdraw() {
        Account account = new Account();
        account.setId("testId");
        account.setBalance(1000.0);

        when(accountRepository.findById("testId")).thenReturn(Optional.of(account));

        Operation operation = operationService.withdraw("testId", 500.0);

        assertEquals(500.0, account.getBalance());
        assertEquals(500.0, operation.getAmount());
    }

    @Test
    public void testWithdrawWithInsufficientFunds() {
        Account account = new Account();
        account.setId("testId");
        account.setBalance(500.0);
        account.setOverDraft(200.0);

        when(accountRepository.findById("testId")).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> {
            operationService.withdraw("testId", 800.0);
        });
    }

    @ParameterizedTest
    @MethodSource("provideWithdrawTestCases")
    public void testWithdraw(double balance, double overDraft, double amount, boolean exceptionExpected) {
        Account account = new Account();
        account.setId("testId");
        account.setBalance(balance);
        account.setOverDraft(overDraft);

        when(accountRepository.findById("testId")).thenReturn(Optional.of(account));

        if (exceptionExpected) {
            assertThrows(InsufficientFundsException.class, () -> {
                operationService.withdraw("testId", amount);
            });
        } else {
            Operation operation = operationService.withdraw("testId", amount);
            assertEquals(balance - amount, account.getBalance());
            assertEquals(amount, operation.getAmount());
        }
    }

    private static Stream<Arguments> provideWithdrawTestCases() {
        return Stream.of(
                Arguments.of(1000.0, 200.0, 500.0, false), // Test case where withdraw is successful
                Arguments.of(500.0, 200.0, 800.0, true)   // Test case where InsufficientFundsException is expected
        );
    }

    @Test
    public void testTransfer() {
        Customer customer = new Customer();
        customer.setId(1L);

        Account fromAccount = new Account();
        fromAccount.setId("fromId");
        fromAccount.setBalance(1000.0);
        fromAccount.setCustomer(customer);

        Account toAccount = new Account();
        toAccount.setId("toId");
        toAccount.setBalance(1000.0);
        toAccount.setCustomer(customer);

        when(accountRepository.findById("fromId")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById("toId")).thenReturn(Optional.of(toAccount));

        Operation operation = operationService.transfer("fromId", "toId", 500.0);

        assertEquals(500.0, fromAccount.getBalance());
        assertEquals(1500.0, toAccount.getBalance());
        assertEquals(500.0, operation.getAmount());
    }

    @Test
    public void testTransferWithInsufficientFunds() {
        Customer customer = new Customer();
        customer.setId(1L);

        Account fromAccount = new Account();
        fromAccount.setId("fromId");
        fromAccount.setBalance(500.0);
        fromAccount.setOverDraft(200.0);
        fromAccount.setCustomer(customer);

        Account toAccount = new Account();
        toAccount.setId("toId");
        toAccount.setBalance(1000.0);
        toAccount.setCustomer(customer);

        when(accountRepository.findById("fromId")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById("toId")).thenReturn(Optional.of(toAccount));

        assertThrows(InsufficientFundsException.class, () -> {
            operationService.transfer("fromId", "toId", 800.0);
        });
    }

    @ParameterizedTest
    @MethodSource("provideTransferTestCases")
    public void testTransfer(double balance, double overDraft, double amount, boolean exceptionExpected) {
        Customer customer = new Customer();
        customer.setId(1L);

        Account fromAccount = new Account();
        fromAccount.setId("fromId");
        fromAccount.setBalance(balance);
        fromAccount.setOverDraft(overDraft);
        fromAccount.setCustomer(customer);

        Account toAccount = new Account();
        toAccount.setId("toId");
        toAccount.setBalance(1000.0);
        toAccount.setCustomer(customer);

        when(accountRepository.findById("fromId")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById("toId")).thenReturn(Optional.of(toAccount));

        if (exceptionExpected) {
            assertThrows(InsufficientFundsException.class, () -> {
                operationService.transfer("fromId", "toId", amount);
            });
        } else {
            Operation operation = operationService.transfer("fromId", "toId", amount);
            assertEquals(balance - amount, fromAccount.getBalance());
            assertEquals(1000.0 + amount, toAccount.getBalance());
            assertEquals(amount, operation.getAmount());
        }
    }

    private static Stream<Arguments> provideTransferTestCases() {
        return Stream.of(
                Arguments.of(1000.0, 200.0, 500.0, false), // Test case where transfer is successful
                Arguments.of(500.0, 200.0, 800.0, true)   // Test case where InsufficientFundsException is expected
        );
    }

    @Test
    public void testGetAccountOperations() {
        String accountId = "testId";
        Operation operation1 = new Operation();
        Operation operation2 = new Operation();
        List<Operation> expectedOperations = Arrays.asList(operation1, operation2);

        when(operationRepository.findAllByAccountId(accountId)).thenReturn(expectedOperations);

        List<Operation> actualOperations = operationService.getAccountOperations(accountId);

        assertEquals(expectedOperations, actualOperations);
    }

    @Test
    public void testGetCustomerOperations() {
        Long customerId = 1L;
        String accountId1 = "testId1";
        String accountId2 = "testId2";
        Account account1 = new Account();
        account1.setId(accountId1);
        Account account2 = new Account();
        account2.setId(accountId2);
        List<Account> accounts = Arrays.asList(account1, account2);

        Operation operation1 = new Operation();
        Operation operation2 = new Operation();
        List<Operation> operations1 = Arrays.asList(operation1);
        List<Operation> operations2 = Arrays.asList(operation2);
        List<Operation> expectedOperations = Arrays.asList(operation1, operation2);

        when(accountRepository.findAllByCustomerId(customerId)).thenReturn(accounts);
        when(operationRepository.findAllByAccountId(accountId1)).thenReturn(operations1);
        when(operationRepository.findAllByAccountId(accountId2)).thenReturn(operations2);

        List<Operation> actualOperations = operationService.getCustomerOperations(customerId);

        assertEquals(expectedOperations, actualOperations);
    }
}