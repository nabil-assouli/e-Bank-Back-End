package com.example.bankappapplication.controller;

import com.example.bankappapplication.dto.CustomerAccountsResponse;
import com.example.bankappapplication.model.Account;
import com.example.bankappapplication.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    public void testCreateAccountForCustomer() throws Exception {
        Long customerId = 1L;
        Account account = new Account();
        account.setBalance(1000);
        account.setOverDraft(500);
        when(accountService.createAccountForCustomer(customerId, account)).thenReturn(account);

        mockMvc.perform(post("/api/accounts/customer/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetAccount() throws Exception {
        String accountId = "1";
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(1000);
        account.setOverDraft(500);
        when(accountService.getAccount(accountId)).thenReturn(account);

        mockMvc.perform(get("/api/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCustomerAccounts() throws Exception {
        Long customerId = 1L;
        when(accountService.getCustomerAccounts(customerId)).thenReturn(new CustomerAccountsResponse());

        mockMvc.perform(get("/api/accounts/customer/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void testDeleteAccount() throws Exception {
        String accountId = "1";

        mockMvc.perform(delete("/api/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteAccount(accountId);
    }
}