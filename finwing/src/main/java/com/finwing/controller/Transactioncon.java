package com.finwing.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finwing.dto.TransactionDto;
import com.finwing.entity.Transaction;
import com.finwing.entity.User;
import com.finwing.repository.TransactionRepository;
import com.finwing.service.TransactionService;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class Transactioncon {

    @Autowired
    TransactionRepository transactionrepository;

    @Autowired
    TransactionService transactionService;

    @GetMapping("/transaction")
    public String showTransaction(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Transaction> transactions = transactionrepository.findByUser(user);
        Map<String, Object> stats = transactionService.getDashboardStats(user);
        model.addAttribute("transactions", transactions);
        model.addAttribute("transactionForm", new TransactionDto()); 
        model.addAttribute("budgetExceeded", stats.get("budgetExceeded"));
        model.addAttribute("monthlyBudget", stats.get("monthlyBudget"));
        model.addAttribute("totalExpense", stats.get("totalExpense"));
        return "transaction";
    }

    @PostMapping("/transaction")
    public String makeTransaction(
            @RequestParam Double amount,
            @RequestParam String type,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam(required = false) LocalDate date,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setDescription(description);
        transaction.setDate(date != null ? date : LocalDate.now());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUser(user);
        transactionrepository.save(transaction);

        return "redirect:/transaction";
    }
}