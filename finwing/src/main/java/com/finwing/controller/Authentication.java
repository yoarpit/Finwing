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


@Controller
public class Authentication {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    // 1. Show the Registration Page
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "registration"; // looks for register.html in templates
    }

    // 2. Handle the Form Submission
    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto, Model model) {
        User user = new User();
         if (userRepository.existsByEmail(registrationDto.getEmail())) {
                
            return "registration";
        }
         if (userRepository.existsByUsername(registrationDto.getUserName())) {
            model.addAttribute("error", "Username already exists");
            return "registration";
        }

        user.setEmail(registrationDto.getEmail());
        user.setPassword(encoder.encode(registrationDto.getPassword()));
        user.setFullName(registrationDto.getFullName());
        user.setCurrency(registrationDto.getCurrency());
        user.setUsername(registrationDto.getUserName());
        user.setGender(registrationDto.getGender());
        user.setMobileNo(registrationDto.getMobileNo());
        user.setMonthlyBudget(
                registrationDto.getMonthlyBudget() != null ? registrationDto.getMonthlyBudget() : 0.0
        );
        user.setIsActive(registrationDto.getIs_active());
        user.setCreatedAt(registrationDto.getCreated_at());
        user.setUpdatedAt(registrationDto.getUpdate_at());
        
        userRepository.save(user); // Hibernate saves this to Postgres
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, 
                            @RequestParam String password, 
                            HttpSession session, 
                            Model model) {

        Optional<User> optionaluser = userRepository.findByEmail(email);

        if (optionaluser.isPresent()) {
          User user = optionaluser.get();

            if (encoder.matches(password, user.getPassword())) {

                
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("user", user);
                return "redirect:/dashboard";
            }

        }
         
            model.addAttribute("error", "Invalid email or password");
            return "login";
        
    }
   

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // destroy session
        return "redirect:/login?logout";
 }
}