package com.finwing.controller;

import com.finwing.service.TransactionService;
import com.finwing.service.UserService;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finwing.dto.AdminRegistractionDto;
import com.finwing.entity.Admin;
import com.finwing.repository.AdminRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TransactionService transactionService;
    private final UserService userService;

    @Autowired
    private AdminRepository adminRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    AdminController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    // ─── CHECK ADMIN SESSION ──────────────────────────────────────────────────
    private boolean isAdminLoggedIn(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("role"));
    }

    // ─── SHOW REGISTER FORM ───────────────────────────────────────────────────
    @GetMapping("/adminRegister")
    public String showRegistrationform(Model model, HttpSession session) {
        if (isAdminLoggedIn(session)) return "redirect:/admin/admindashboard";
        model.addAttribute("admin", new AdminRegistractionDto());
        return "Admin/adminRegistration";  // ✅ FIXED: was "Admin/adminRegistration"
    }

    // ─── HANDLE REGISTER ──────────────────────────────────────────────────────
    @PostMapping("/register")
    public String registerAdmin(@ModelAttribute("admin") AdminRegistractionDto dto, Model model) {

        if (adminRepository.existsByEmail(dto.getEmail())) {
            model.addAttribute("error", "Email already exists");
            model.addAttribute("admin", dto);
            return "Admin/adminRegistration";
        }

        if (adminRepository.existsByAdminName(dto.getAdminName())) {
            model.addAttribute("error", "Username already exists");
            model.addAttribute("admin", dto);
            return "Admin/adminRegistration";
        }

        Admin admin = new Admin();
        admin.setAdminName(dto.getAdminName());
        admin.setEmail(dto.getEmail());
        admin.setPassword(encoder.encode(dto.getPassword()));
        admin.setIsActive(dto.getIs_active());
        admin.setCreatedAt(dto.getCreated_at());
        admin.setUpdatedAt(dto.getUpdate_it());

        adminRepository.save(admin);

        return "redirect:/admin/login?success";
    }

    // ─── SHOW REGISTER PAGE (GET /admin/register) ─────────────────────────────
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("admin", new AdminRegistractionDto());
        return "Admin/adminRegistration";
    }

    // ─── SHOW LOGIN ───────────────────────────────────────────────────────────
    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session,
                                @RequestParam(required = false) String success,
                                @RequestParam(required = false) String logout) {
        if (isAdminLoggedIn(session)) return "redirect:/admin/admindashboard";
        if (success != null) model.addAttribute("success", "Admin account created! Please log in.");
        if (logout != null)  model.addAttribute("success", "Logged out successfully.");
        return "Admin/admin-login";
    }

    // ─── HANDLE LOGIN ─────────────────────────────────────────────────────────
    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {

        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            if (encoder.matches(password, admin.getPassword())) {
                session.setAttribute("adminId",   admin.getAdminId());
                session.setAttribute("adminName", admin.getAdminName()); // ✅ FIXED: capital N
                session.setAttribute("admin",     admin);
                session.setAttribute("role",      "ADMIN");
                return "redirect:/admin/admindashboard";
            }
        }

        model.addAttribute("error", "Invalid email or password.");
        return "Admin/admin-login";  
    }

    // ─── ADMIN DASHBOARD ──────────────────────────────────────────────────────
    @GetMapping("/admindashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";

        String adminName = (String) session.getAttribute("adminName");
        if (adminName == null) {
            Admin admin = (Admin) session.getAttribute("admin");
            adminName = admin != null ? admin.getAdminName() : "Admin";
        }

        model.addAttribute("adminName", adminName);
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("allTransactions", transactionService.getAllTransactions());
        model.addAttribute("userCount", userService.getAllUsers().size());
        model.addAttribute("txCount", transactionService.getAllTransactions().size());
        model.addAttribute("adminCount", adminRepository.findAll().size());
        return "Admin/admin-dashboard";
    }

    // ─── DELETE USER ──────────────────────────────────────────────────────────
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable long id, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        userService.deleteUser(id);
        return "redirect:/admin/admindashboard?deleted";  // ✅ FIXED: was /admin/dashboard
    }

    // ─── LOGOUT ───────────────────────────────────────────────────────────────
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login?logout";
    }
}