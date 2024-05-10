package com.example.bankappapplication.service;

import com.example.bankappapplication.exception.CustomerNotFoundException;
import com.example.bankappapplication.model.Customer;
import com.example.bankappapplication.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  @Autowired
        private CustomerRepository customerRepository;

        public Customer createCustomer(Customer customer) {
            return customerRepository.save(customer);
        }

        public Customer getCustomer(Long customerId) {
            return customerRepository.findById(customerId)
                    .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        }

        public Customer getCustomerByEmail(String email) {
            return customerRepository.findCustomerByEmail(email)
                    .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
        }

        public void deleteCustomer(Long customerId) {
            customerRepository.deleteById(customerId);
        }

}
