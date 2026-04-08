package com.finwing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.finwing.repository.TransactionRepository;
import com.finwing.entity.Transaction;
import com.finwing.dto.DashboardDto;
import com.finwing.dto.TransactionDto;

import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private TransactionRepository repo;

    public DashboardDto getDashboard(Long userId) {

        List<Transaction> list = repo.findByUserUserId(userId);

        double income = 0;
        double expense = 0;
        Map<String, Double> categoryMap = new HashMap<>();

        for (Transaction t : list) {
            if ("INCOME".equalsIgnoreCase(t.getType())) {
                income += t.getAmount();
            } else {
                expense += t.getAmount();

                categoryMap.put(
                    t.getCategory(),
                    categoryMap.getOrDefault(t.getCategory(), 0.0) + t.getAmount()
                );
            }
        }

        double balance = income - expense;
        double savingsRate = income == 0 ? 0 : (balance / income) * 100;

        DashboardDto dto = new DashboardDto();
        dto.setTotalIncome(income);
        dto.setTotalExpense(expense);
        dto.setBalance(balance);
        dto.setSavingsRate(savingsRate);
        dto.setCategoryExpenses(categoryMap);
        // dto.setRecentTransactions(list.stream().limit(5).toList());
        dto.setRecentTransactions(
    list.stream()
        .limit(5)
        .map(t -> {
            TransactionDto td = new TransactionDto();
            td.setCategory(t.getCategory());
            td.setAmount(t.getAmount());
            td.setType(t.getType());
            return td;
        })
        .toList());

        dto.setInsight(getInsight(income, expense));

        return dto;
    }

    private String getInsight(double income, double expense) {
        if (expense > income) return "⚠ Spending exceeds income!";
        if (expense > income * 0.7) return "⚡ High spending detected.";
        return "✅ Financial health is good.";
    }
}