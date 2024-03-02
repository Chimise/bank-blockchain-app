package com.firstacademy.firstblock.service;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.firstacademy.firstblock.dto.model.UserDto;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    public Authentication authenticate(String email, String password) throws AuthenticationException;

    public String generateJwt(String username, List<String> roles);

    public void setJwtCookie(HttpServletResponse response, Authentication auth);
}
