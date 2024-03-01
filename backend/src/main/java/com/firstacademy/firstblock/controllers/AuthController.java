package com.firstacademy.firstblock.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.firstacademy.firstblock.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import com.firstacademy.firstblock.dto.model.UserDto;
import com.firstacademy.firstblock.dto.response.Response;
import com.firstacademy.firstblock.dto.request.SignUpDto;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Response<?> logInUser(HttpServletRequest req, HttpServletResponse res, @Valid @RequestBody SignUpDto body) {

        Authentication authentication = authService.authenticate(body.getEmail(), body.getPassword());
        authService.setJwtCookie(res, authentication);

        return Response.ok().setPayload("Successfully signed in");
    }
}