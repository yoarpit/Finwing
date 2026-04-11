package com.finwing.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionDto {

    private Double amount;
    private String type;       
    private String category;
    private String description;
    private LocalDate date;
    
}