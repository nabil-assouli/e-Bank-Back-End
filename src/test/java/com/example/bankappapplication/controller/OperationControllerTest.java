package com.example.bankappapplication.controller;

import com.example.bankappapplication.exception.InsufficientFundsException;
import com.example.bankappapplication.model.Operation;
import com.example.bankappapplication.service.OperationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OperationController.class)
public class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OperationController operationController;

    @MockBean
    private OperationService operationService;

    @BeforeEach
    public void setup() {
        // Configure the mock behavior for operationService
        when(operationService.deposit(anyString(), anyDouble())).thenReturn(null);
        when(operationService.withdraw(anyString(), anyDouble())).thenReturn(null);
        when(operationService.transfer(anyString(), anyString(), anyDouble())).thenReturn(null);
    }

    @Test
    public void testDeposit() throws Exception {
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("amount", 500.0);

        mockMvc.perform(post("/api/operations/deposit/testId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    public void testWithdraw() throws Exception {
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("amount", 500.0);

        mockMvc.perform(post("/api/operations/withdraw/testId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    public void testWithdrawWithInsufficientFunds() throws Exception {
        String accountId = "testId";
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("amount", 800.0);

        when(operationService.withdraw(anyString(), anyDouble())).thenThrow(new InsufficientFundsException(accountId));

        mockMvc.perform(post("/api/operations/withdraw/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testTransfer() throws Exception {
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("amount", 500.0);

        mockMvc.perform(post("/api/operations/transfer/fromId/toId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }


    @Test
    public void testTransferWithInsufficientFunds() throws Exception {
        String fromAccountId = "fromId";
        String toAccountId = "toId";
        Map<String, Double> requestBody = new HashMap<>();
        requestBody.put("amount", 800.0);

        when(operationService.transfer(anyString(), anyString(), anyDouble())).thenThrow(new InsufficientFundsException(fromAccountId));

        mockMvc.perform(post("/api/operations/transfer/" + fromAccountId + "/" + toAccountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAccountOperations() throws Exception {
        String accountId = "testId";
        Operation operation1 = new Operation();
        Operation operation2 = new Operation();
        List<Operation> operations = Arrays.asList(operation1, operation2);

        when(operationService.getAccountOperations(accountId)).thenReturn(operations);

        mockMvc.perform(get("/api/operations/history/account/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(operations)));
    }

    @Test
    public void testGetCustomerOperations() throws Exception {
        Long customerId = 1L;
        Operation operation1 = new Operation();
        Operation operation2 = new Operation();
        List<Operation> operations = Arrays.asList(operation1, operation2);

        when(operationService.getCustomerOperations(customerId)).thenReturn(operations);

        mockMvc.perform(get("/api/operations/history/customer/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(operations)));
    }
}