package com.example.bankappapplication.dto;

import com.example.bankappapplication.model.OperationType;
import lombok.Data;

import java.util.Date;

@Data
public class OperationDTO {
    private Long id;
    private Date operationDate;
    private OperationType type;
    private double amount;
    private String accountId;

}