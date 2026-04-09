package com.finwing.controller;

import java.util.Optional;

import org.aspectj.weaver.BCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

// import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("/adminRegister")
    public String showRegistrationform(Model model){
        model.addAttribute("msg", new AdminRegistractionDto());
        return "adminRegistration";
    }
    @GetMapping("/admin-login")
public String adminLogin() {
    return "Admin/admin-login"; // templates/Admin/admin-login.html
}

    @PostMapping("/adminRegister")
    public String registerAdmin(@ModelAttribute("admin") AdminRegistractionDto adminregistrationDto, Model model) {

        if (adminRepository.existsByEmail(adminregistrationDto.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        if (adminRepository.existsByAdminName(adminregistrationDto.getAdminName())) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        Admin admin = new Admin();
        admin.setAdminName(adminregistrationDto.getAdminName());   
        admin.setEmail(adminregistrationDto.getEmail());
        admin.setPassword(encoder.encode(adminregistrationDto.getPassword()));
        admin.setIsActive(adminregistrationDto.getIs_active());
        admin.setCreatedAt(adminregistrationDto.getCreated_at());
        admin.setUpdatedAt(adminregistrationDto.getUpdate_it());

        adminRepository.save(admin);

        return "redirect:/register?success";
    }
    @PostMapping("/loginAdmin")
    public String postMethodName(@RequestParam String email,
        @RequestParam String password,HttpSession session,Model model
    ) {
        Optional<Admin> optionaladmin = adminRepository.findByEmail(email);
        if (optionaladmin.isPresent()) {
            Admin admin = optionaladmin.get();
            if (encoder.matches(password, admin.getPassword())) {
                session.setAttribute("userId", admin.getAdminId());
                session.setAttribute("username", admin.getAdminName());
                session.setAttribute("admin", admin);
                return "redirect:/admindashboard?loginAdmin";
            }
        }
        
        return "loginAdmin";
    }
    
}



// @RestController
// public class Authentication {

//     @Autowired
//     private UserRepository userRepository;

//     private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


//     // 1. Show the Registration Page
//     @GetMapping("/register")
//     public String showRegistrationForm(Model model) {
//         model.addAttribute("user", new UseradminregistrationDto());
//         return "register"; // looks for register.html in templates
//     }

//     // 2. Handle the Form Submission
    
//     @PostMapping("/login")
//     public String loginUser(@RequestParam String email, 
//                             @RequestParam String password, 
//                             HttpSession session, 
//                             Model model) {

//         Optional<User> optionaluser = userRepository.findByEmail(email);

//         if (optionaluser.isPresent()) {
//           User user = optionaluser.get();

//             if (encoder.matches(password, user.getPassword())) {

                
//                 session.setAttribute("userId", user.getUserId());
//                 session.setAttribute("username", user.getUsername());
//                 session.setAttribute("user", user);
//                 return "redirect:/dashboard";
//             }

//         }
         
//             model.addAttribute("error", "Invalid email or password");
//             return "login";
        
//     }


//     @GetMapping("/logout")
//     public String logout(HttpSession session) {
//         session.invalidate(); // destroy session
//         return "redirect:/login?logout";
//  }
// }