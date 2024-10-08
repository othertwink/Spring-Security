package org.othertwink.employeeapp.controller;

import org.othertwink.employeeapp.model.entity.User;
import org.othertwink.employeeapp.security.service.OurUserDetailedService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final OurUserDetailedService ourUserDetailedService;

    public AdminController(OurUserDetailedService ourUserDetailedService) {
        this.ourUserDetailedService = ourUserDetailedService;
    }

    @PostMapping("/unlock/{username}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> unlockUser(@PathVariable String username) {
        User user = (User) ourUserDetailedService.loadUserByUsername(username);
        ourUserDetailedService.resetFailedLoginAttempts(user);
        return ResponseEntity.ok("User " + username + " has been unlocked.");
    }
}
