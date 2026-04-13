package com.finwing.controller;
import com.finwing.dto.TransactionDto;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finwing.dto.TransactionDto;
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
      model.addAttribute("transactionForm", new TransactionDto());
      model.addAttribute("currentMonth", "March 2026");

      return "transaction";       
 
}


// @PostMapping("/transaction/add")
// public String makeTransaction( @RequestParam Double amount,
//             @RequestParam String type,
//             @RequestParam String category,
//             @RequestParam String description,
//             HttpSession session){
     
//       User user = (User) session.getAttribute("user");

//         if (user == null) {
//             return "redirect:/login";
//         }
//         Transaction transaction=new Transaction();
//         transaction.setAmount(amount);
//         transaction.setType(type);
//         transaction.setCategory(category);
//         transaction.setDescription(description);
//         transaction.setCreatedAt(LocalDateTime.now());
//         transaction.setUser(user);
//         transactionrepository.save(transaction);
        
//         return "redirect:/transaction";
   
// } 

@PostMapping("/transaction")
public String makeTransaction(@ModelAttribute TransactionDto dto,
                             HttpSession session){

    User user = (User) session.getAttribute("user");

    if (user == null) {
        return "redirect:/login";
    }

    Transaction transaction = new Transaction();

    transaction.setAmount(dto.getAmount());
    transaction.setType(dto.getType());
    transaction.setCategory(dto.getCategory());
    transaction.setDescription(dto.getDescription());

    if(dto.getDate() != null){
        transaction.setCreatedAt(dto.getDate().atStartOfDay());
    } else {
        transaction.setCreatedAt(LocalDateTime.now());
    }

    transaction.setUser(user);

    transactionrepository.save(transaction);

    return "redirect:/transaction";
}
}
