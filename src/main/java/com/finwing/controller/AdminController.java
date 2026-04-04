package com.finwing.controller;

import org.aspectj.weaver.BCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finwing.dto.AdminRegistractionDto;
import com.finwing.dto.UserRegistrationDto;
import com.finwing.entity.Admin;
import com.finwing.repository.AdminRepository;
import com.finwing.repository.UserRepository;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

@RestController
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;
    private BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

    @GetMapping("/adminRegister")
    public String showRegistrationform(Model model){
        model.addAttribute("msg", new AdminRegistractionDto());
        return "adminRegistration";
    }


    @PostMapping("/adminRegister")
    public String registerAdmin(@ModelAttribute("admin") AdminRegistractionDto adminregistrationDto, Model model) {
        Admin admin = new Admin();
         if (AdminRepository.existsByUsername(AdminRegistractionDto.get())) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }
         if (userRepository.existsByUsername(registrationDto.getUserName())) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        admin.setEmail(registrationDto.getEmail());
        admin.setPassword(encoder.encode(registrationDto.getPassword()));
        admin.setFullName(registrationDto.getFullName());
        admin.setCurrency(registrationDto.getCurrency());
        admin.setUsername(registrationDto.getUserName());
        admin.setGender(registrationDto.getGender());
        admin.setMobileNo(registrationDto.getMobile_no());
        admin.setIsActive(registrationDto.getIs_active());
        admin.setCreatedAt(registrationDto.getCreated_at());
        admin.setUpdatedAt(registrationDto.getUpdate_it());
        
        userRepository.save(user); // Hibernate saves this to Postgres
        return "redirect:/register?success";
    }


}



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