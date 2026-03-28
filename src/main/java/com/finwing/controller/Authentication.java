package com.finwing.controller;

import com.finwing.dto.UserRegistrationDto;
import com.finwing.entity.User;
import com.finwing.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
public class Authentication {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    // 1. Show the Registration Page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register"; // looks for register.html in templates
    }

    // 2. Handle the Form Submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto, Model model) {
        User user = new User();
         if (userRepository.existsByEmail(registrationDto.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }
         if (userRepository.existsByUsername(registrationDto.getUserName())) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        user.setEmail(registrationDto.getEmail());
        user.setPassword(encoder.encode(registrationDto.getPassword()));
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

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, 
                            @RequestParam String password, 
                            HttpSession session, 
                            Model model) {
        
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            // Store user in session to keep them "logged in"
            session.setAttribute("loggedInUser", user.get());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
    session.invalidate(); // destroy session
    return "redirect:/login?logout";
 }
}