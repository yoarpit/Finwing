package com.finwing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class error {
    
    @GetMapping("/Error")
    public String getError(Model model){
        model.addAttribute("msg","error");
        return "error";
    }
    
}
