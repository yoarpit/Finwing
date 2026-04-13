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

        Object userObj = session.getAttribute("userId");

        if (userObj == null) {
            throw new RuntimeException("User not logged in");
        }

        Long userId;

        try {
            userId = Long.valueOf(userObj.toString());
        } catch (Exception e) {
            throw new RuntimeException("Invalid session userId");
        }

        return service.getDashboard(userId);
    }
}