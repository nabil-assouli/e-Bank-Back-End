package com.example.bankappapplication.controller;

import com.example.bankappapplication.model.Customer;
import com.example.bankappapplication.service.CustomerService;
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
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testCreateCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setName("Nabil");
        customer.setEmail("nabil@example.com");
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetCustomer() throws Exception {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Nabil");
        customer.setEmail("nabil@example.com");
        when(customerService.getCustomer(customerId)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCustomerByEmail() throws Exception {
        String email = "nabil@example.com";
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Nabil");
        customer.setEmail(email);
        when(customerService.getCustomerByEmail(email)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/email/" + email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        Long customerId = 1L;

        mockMvc.perform(delete("/api/customers/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(customerService, times(1)).deleteCustomer(customerId);

    }

}