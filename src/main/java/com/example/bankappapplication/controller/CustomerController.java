package com.example.bankappapplication.controller;

import com.example.bankappapplication.dto.CustomerDTO;
import com.example.bankappapplication.exception.CustomerNotFoundException;
import com.example.bankappapplication.mapper.CustomerMapper;
import com.example.bankappapplication.model.Customer;
import com.example.bankappapplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

        @Autowired
        private CustomerService customerService;

        @Autowired
        private CustomerMapper customerMapper;

        @PostMapping
        public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
            Customer customer = mapToCustomer(customerDTO);
            Customer savedCustomer = customerService.createCustomer(customer);
            CustomerDTO savedCustomerDTO = mapToCustomerDTO(savedCustomer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomerDTO);
        }

        @GetMapping("/{customerId}")
        public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long customerId) {
            CustomerDTO customerDTO = customerService.getCustomerDTO(customerId);
            return ResponseEntity.ok(customerDTO);
        }

        @DeleteMapping("/{customerId}")
        public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
            customerService.deleteCustomer(customerId);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/email/{email}")
        public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
            CustomerDTO customerDTO = customerService.getCustomerDTOByEmail(email);
            return ResponseEntity.ok(customerDTO);
        }

        private CustomerDTO mapToCustomerDTO(Customer customer) {
            return customerMapper.mapToCustomerDTO(customer);
        }

        private Customer mapToCustomer(CustomerDTO customerDTO) {
            return customerMapper.mapToCustomer(customerDTO);
        }

        @ExceptionHandler(CustomerNotFoundException.class)
        public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
}

