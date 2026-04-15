package com.finwing.service;

import com.finwing.dto.TransactionDto;
import com.finwing.entity.Transaction;
import com.finwing.entity.User;
import com.finwing.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    // Add a single transaction
    public Transaction addTransaction(TransactionDto dto, User user) {
        Transaction tx = new Transaction();
        tx.setAmount(dto.getAmount());
        tx.setType(dto.getType());
        tx.setCategory(dto.getCategory());
        tx.setDescription(dto.getDescription());
         tx.setDate(dto.getDate() != null ? dto.getDate() : LocalDate.now());
        tx.setCreatedAt(dto.getDate() != null ? dto.getDate().atStartOfDay() : LocalDateTime.now());
        tx.setUser(user);

       return transactionRepository.save(tx);
       
    }

    // Get all transactions for user
    public List<Transaction> getUserTransactions(User user) {
        return transactionRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // Delete transaction (only if it belongs to user)
    public boolean deleteTransaction(Long id, User user) {
        Optional<Transaction> opt = transactionRepository.findById(id);
        if (opt.isPresent() && opt.get().getUser().getUserId().equals(user.getUserId())) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Get single transaction if it belongs to logged-in user
    public Transaction getUserTransactionById(Long id, User user) {
        Optional<Transaction> opt = transactionRepository.findById(id);
        if (opt.isPresent() && opt.get().getUser().getUserId().equals(user.getUserId())) {
            return opt.get();
        }
        return null;
    }

    // Update transaction (only if it belongs to user)
    public boolean updateTransaction(
            Long id,
            Double amount,
            String type,
            String category,
            String description,
            LocalDate date,
            User user) {
        Optional<Transaction> opt = transactionRepository.findById(id);
        if (opt.isEmpty()) {
            return false;
        }

        Transaction tx = opt.get();
        if (!tx.getUser().getUserId().equals(user.getUserId())) {
            return false;
        }

        tx.setAmount(amount);
        tx.setType(type);
        tx.setCategory(category);
        tx.setDescription(description);
        tx.setDate(date);
        transactionRepository.save(tx);
        return true;
    }

    // Dashboard summary stats
    public Map<String, Object> getDashboardStats(User user) {
        Map<String, Object> stats = new HashMap<>();

        Double totalIncome = transactionRepository.sumAmountByUserAndType(user, "income");
        Double totalExpense = transactionRepository.sumAmountByUserAndType(user, "expense");

        totalIncome = totalIncome != null ? totalIncome : 0.0;
        totalExpense = totalExpense != null ? totalExpense : 0.0;

        double netBalance = totalIncome - totalExpense;
        double savingsRate = totalIncome > 0 ? ((netBalance / totalIncome) * 100) : 0;
        double monthlyBudget = user.getMonthlyBudget() != null ? user.getMonthlyBudget() : 0.0;
        boolean budgetExceeded = monthlyBudget > 0 && totalExpense > monthlyBudget;
        double budgetRemaining = monthlyBudget - totalExpense;

        stats.put("totalIncome", totalIncome);
        stats.put("totalExpense", totalExpense);
        stats.put("netBalance", netBalance);
        stats.put("savingsRate", String.format("%.1f", savingsRate));
        stats.put("monthlyBudget", monthlyBudget);
        stats.put("budgetExceeded", budgetExceeded);
        stats.put("budgetRemaining", budgetRemaining);

        // Category breakdown
        List<Object[]> breakdown = transactionRepository.sumExpensesByCategory(user);
        Map<String, Double> categoryMap = new LinkedHashMap<>();
        for (Object[] row : breakdown) {
            categoryMap.put((String) row[0], (Double) row[1]);
        }
        stats.put("categoryBreakdown", categoryMap);

        // Recent 6 transactions
        List<Transaction> allTx = transactionRepository.findByUserOrderByCreatedAtDesc(user);
        stats.put("recentTransactions", allTx.size() > 6 ? allTx.subList(0, 6) : allTx);
        stats.put("totalCount", allTx.size());

        // Chart data (latest 30 transactions)
        List<Map<String, Object>> chartTransactions = new ArrayList<>();
        int chartLimit = Math.min(allTx.size(), 30);
        for (int i = chartLimit - 1; i >= 0; i--) {
            Transaction tx = allTx.get(i);
            Map<String, Object> point = new LinkedHashMap<>();
            point.put("date", tx.getDate() != null ? tx.getDate().toString() : "");
            point.put("type", tx.getType());
            point.put("category", tx.getCategory());
            point.put("amount", tx.getAmount() != null ? tx.getAmount() : 0.0);
            chartTransactions.add(point);
        }
        stats.put("chartTransactions", chartTransactions);

        return stats;
    }

    

    // Admin: all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllByOrderByCreatedAtDesc();
    }
}
