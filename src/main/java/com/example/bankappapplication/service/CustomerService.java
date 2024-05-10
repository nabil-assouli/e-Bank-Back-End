package com.example.bankappapplication.service;

import com.example.bankappapplication.dto.CustomerDTO;
import com.example.bankappapplication.exception.CustomerNotFoundException;
import com.example.bankappapplication.mapper.CustomerMapper;
import com.example.bankappapplication.model.Customer;
import com.example.bankappapplication.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public CustomerDTO getCustomerDTO(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        return mapToCustomerDTO(customer);
    }

    public CustomerDTO getCustomerDTOByEmail(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
        return mapToCustomerDTO(customer);
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    private CustomerDTO mapToCustomerDTO(Customer customer) {
        return customerMapper.mapToCustomerDTO(customer);
    }
}
