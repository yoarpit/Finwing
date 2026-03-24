package com.finwing.controller;

import com.finwing.dto.UserRegistrationDto;
import com.finwing.entity.User;
import com.finwing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class Authentication {

    @Autowired
    private UserRepository userRepository;

    // 1. Show the Registration Page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register"; // looks for register.html in templates
    }

    // 2. Handle the Form Submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword()); // Note: Always hash passwords in production!
        user.setFullName(registrationDto.getFullName());
        user.setCurrency(registrationDto.getCurrency());
        user.setUsername(registrationDto.getUserName());
        user.setGender(registrationDto.getGender());
        user.setMobileNo(registrationDto.getMobile_no());
        user.setIsActive(registrationDto.getIs_active());
        user.setCreatedAt(registrationDto.getCreated_at());
        user.setUpdatedAt(registrationDto.getUpdate_it());
        
        userRepository.save(user); // Hibernate saves this to Postgres
        return "redirect:/register?success";
    }
}