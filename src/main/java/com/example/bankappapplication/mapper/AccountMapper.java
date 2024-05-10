package com.example.bankappapplication.mapper;

import com.example.bankappapplication.dto.AccountDTO;
import com.example.bankappapplication.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDTO mapToAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setOverDraft(account.getOverDraft());
        accountDTO.setCreatedAt(account.getCreatedAt());
        return accountDTO;
    }

    public Account mapToAccount(AccountDTO accountDTO) {
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setBalance(accountDTO.getBalance());
        account.setOverDraft(accountDTO.getOverDraft());
        account.setCreatedAt(accountDTO.getCreatedAt());
        return account;
    }
}