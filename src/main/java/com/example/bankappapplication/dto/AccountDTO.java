package com.example.bankappapplication.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AccountDTO {
    private String id;
    private double balance;
    private double overDraft;
    private Date createdAt;
}