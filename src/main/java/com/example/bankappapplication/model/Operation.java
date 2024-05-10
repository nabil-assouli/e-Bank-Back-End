package com.example.bankappapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Operation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    private double amount;
    @ManyToOne
    @JsonIgnoreProperties(value = {"operations", "customer"})
    private Account account;

}
