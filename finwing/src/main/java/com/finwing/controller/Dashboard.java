package com.finwing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.finwing.entity.User;
import com.finwing.repository.UserRepository;
import com.finwing.service.TransactionService;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class Dashboard {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(((Number) userId).longValue()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        Map<String, Object> stats = transactionService.getDashboardStats(user);
        stats.forEach(model::addAttribute);

        return "Dashboard";
    }
}