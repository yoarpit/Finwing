package com.finwing.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.finwing.entity.User;
import com.finwing.repository.UserRepository;
import com.finwing.service.TransactionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class Dashboard {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {

        Object userIdObj = session.getAttribute("userId");

        if (userIdObj == null) {
            return "redirect:/login";
        }

        Long userId = ((Number) userIdObj).longValue();

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        Map<String, Object> stats = transactionService.getDashboardStats(user);
        stats.forEach(model::addAttribute);

        return "dashboard";
    }
}