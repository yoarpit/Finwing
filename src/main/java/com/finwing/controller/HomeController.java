package com.finwing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "Finwing");
        model.addAttribute("message", "Welcome to Finwing ");
        return "index"; // loads index.html
    }

    @GetMapping("/login")
    public String Login(Model model){
        return "login";
    }
}

