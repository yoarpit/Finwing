package com.finwing.dto;

import lombok.Data;

@Data
public class TransactionDto {

    private Double amount;
    private String type;       
    private String category;
    private String description;
    
}