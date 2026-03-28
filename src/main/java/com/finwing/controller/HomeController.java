package com.finwing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
  

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "Finwing");
        model.addAttribute("message", "Welcome to Finwing ");
        return "index"; // loads index.html
    }

@GetMapping("/login")
public String login(Model model) {
    model.addAttribute("msg", "Welcome");
    return "login";
}
// @GetMapping("/dashboard")
// public String Dashboard(HttpSession session,Model model){
//      if (session.getAttribute("loggedInUser") == null) {
//             return "redirect:/login";
//         }
//     model.addAttribute("msg","dashboard");
//     return "Dashboard";
// }
@GetMapping("/transaction")
public String transaction(Model model){
    model.addAttribute("msg","transaction");
    return "transaction";
}
@GetMapping("/registration")
public String showRegisterPage() {
    return "registration"; 
}


    

}

