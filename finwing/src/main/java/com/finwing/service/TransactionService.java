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

    // Dashboard summary stats
    public Map<String, Object> getDashboardStats(User user) {
        Map<String, Object> stats = new HashMap<>();

        Double totalIncome = transactionRepository.sumAmountByUserAndType(user, "income");
        Double totalExpense = transactionRepository.sumAmountByUserAndType(user, "expense");

        totalIncome = totalIncome != null ? totalIncome : 0.0;
        totalExpense = totalExpense != null ? totalExpense : 0.0;

        double netBalance = totalIncome - totalExpense;
        double savingsRate = totalIncome > 0 ? ((netBalance / totalIncome) * 100) : 0;

        stats.put("totalIncome", totalIncome);
        stats.put("totalExpense", totalExpense);
        stats.put("netBalance", netBalance);
        stats.put("savingsRate", String.format("%.1f", savingsRate));

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

        return stats;
    }

    

    // Admin: all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllByOrderByCreatedAtDesc();
    }
}
