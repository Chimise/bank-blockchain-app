package com.firstacademy.firstblock.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firstacademy.firstblock.dto.model.UserDto;
import com.firstacademy.firstblock.dto.request.UpdateProfileRequest;
import com.firstacademy.firstblock.dto.response.Response;
import com.firstacademy.firstblock.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Response<?> viewProfile() {
        UserDto currentUser = this.userService.getCurrentUser();
        return Response.ok().setPayload(currentUser);
    }

    @PutMapping
    public Response<?> updateProfile(@Valid @RequestBody UpdateProfileRequest profileRequest) {
        UserDto currentUser = this.userService.getCurrentUser();
        UserDto updatedUser = profileRequest.build(currentUser);
        updatedUser = userService.updateProfile(updatedUser);
        return Response.ok().setPayload(updatedUser);
    }
}
