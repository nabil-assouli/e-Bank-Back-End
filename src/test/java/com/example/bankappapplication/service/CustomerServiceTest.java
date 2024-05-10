package com.example.bankappapplication.service;

import com.example.bankappapplication.model.Customer;
import com.example.bankappapplication.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testgetCustomer() {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Nabil");
        customer.setEmail("nabil@example.com");
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        Customer retrievedCustomer = customerService.getCustomer(customerId);

        // Assert
        assertEquals(customer, retrievedCustomer);
    }

    @Test
    public void testCreateCustomer() {
        // Arrange
        Customer customer = new Customer();
        customer.setName("Nabil");
        customer.setEmail("nabil@example.com");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        Customer createdCustomer = customerService.createCustomer(customer);

        // Assert
        assertEquals(customer, createdCustomer);
    }

    @Test
    public void testGetCustomerByEmail() {
        // Arrange
        String email = "nabil@example.com";
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Nabil");
        customer.setEmail(email);
        when(customerRepository.findCustomerByEmail(email)).thenReturn(Optional.of(customer));

        // Act
        Customer retrievedCustomer = customerService.getCustomerByEmail(email);

        // Assert
        assertEquals(customer, retrievedCustomer);
    }

    @Test
    public void testDeleteCustomer() {
        // Arrange
        Long customerId = 1L;
        doNothing().when(customerRepository).deleteById(customerId);

        // Act
        customerService.deleteCustomer(customerId);

        // Assert
        verify(customerRepository, times(1)).deleteById(customerId);
    }
}