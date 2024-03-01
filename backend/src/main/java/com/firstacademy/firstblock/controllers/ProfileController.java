package com.firstacademy.firstblock.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firstacademy.firstblock.dto.model.UserDto;
import com.firstacademy.firstblock.dto.response.Response;
import com.firstacademy.firstblock.service.UserService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Response<?> viewProfile() {
        UserDto currentUser = this.userService.getCurrentUser();
        return Response.ok().setPayload(currentUser);
    }
}
