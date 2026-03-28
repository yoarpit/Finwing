package com.finwing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import com.finwing.dto.TransactionDto;
import com.finwing.repository.TransactionRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class Dashboard {
    @Autowired TransactionRepository transactionRepository;

    @GetMapping("/dashboard")
    public String ShowDashbord(HttpSession session,Model model){
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";}
        model.addAttribute("transaction", new TransactionDto());
        return "dashboard";
    
}
    

}
