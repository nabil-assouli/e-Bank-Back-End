package com.example.bankappapplication.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String accountId) {
        super("Insufficient funds in account with id: " + accountId);
    }
}