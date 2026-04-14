package com.finwing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.finwing.dto.DashboardDto;
import com.finwing.service.DashboardService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestController {

    @Autowired
    private DashboardService service;

    @GetMapping
    public DashboardDto getDashboard(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("User not logged in");
        }

        return service.getDashboard(userId);
    }
}