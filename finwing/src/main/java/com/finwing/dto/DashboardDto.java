package com.finwing.dto;


import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class DashboardDto{

    private double totalIncome;
    private double totalExpense;
    private double balance;
    private double savingsRate;

    private List<TransactionDto> recentTransactions;
    private Map<String, Double> categoryExpenses;

    private String insight;

    // getters and setters
} 
