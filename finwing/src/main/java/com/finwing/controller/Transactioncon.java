package com.finwing.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finwing.entity.Transaction;
import com.finwing.entity.User;
import com.finwing.repository.TransactionRepository;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;


@Controller
public class Transactioncon {
   @Autowired 
   TransactionRepository transactionrepository;

@GetMapping("/transaction")
public String showTransaction(Model model,HttpSession session){
      User user = (User) session.getAttribute("user");

      if (user == null) {
        return "redirect:/login";
      }
      List<Transaction> transactions = transactionrepository.findByUser(user);

      model.addAttribute("transactions", transactions);
      model.addAttribute("newTx", new Transaction());

      return "transaction";       
 
}


@PostMapping("/transaction")
public String makeTransaction( @RequestParam Double amount,
            @RequestParam String type,
            @RequestParam String category,
            @RequestParam String description,
            HttpSession session){
     
      User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        Transaction transaction=new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setDescription(description);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUser(user);
        transactionrepository.save(transaction);
        
        return "redirect:/transaction";
   
}




    
}
