package com.firstacademy.firstblock.controllers;

import java.net.http.HttpRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.firstacademy.firstblock.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.firstacademy.firstblock.dto.model.UserDto;
import com.firstacademy.firstblock.dto.response.Response;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Response<?> logInUser(HttpServletRequest req, HttpServletResponse res, @RequestBody UserDto userDto) {

        Authentication authentication = authService.authenticate(userDto);
        authService.setJwtCookie(res, authentication);

        return Response.ok().setPayload("Request Successfull");
    }

}