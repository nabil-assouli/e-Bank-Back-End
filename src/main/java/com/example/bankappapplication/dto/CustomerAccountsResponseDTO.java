package com.example.bankappapplication.dto;

import com.example.bankappapplication.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CustomerAccountsResponseDTO {
    private CustomerDTO  customer;
    private List<AccountDTO> accounts;

    // Getters and setters
}