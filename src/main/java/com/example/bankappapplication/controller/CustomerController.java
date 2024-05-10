package com.example.bankappapplication.controller;

import com.example.bankappapplication.exception.CustomerNotFoundException;
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

        @PostMapping
        public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
        }
        @GetMapping("/{customerId}")
        public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId) {
            return ResponseEntity.ok(customerService.getCustomer(customerId));
        }

        @DeleteMapping("/{customerId}")
        public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
            customerService.deleteCustomer(customerId);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/email/{email}")
        public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
            return ResponseEntity.ok(customerService.getCustomerByEmail(email));
        }

        @ExceptionHandler(CustomerNotFoundException.class)
        public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
}
