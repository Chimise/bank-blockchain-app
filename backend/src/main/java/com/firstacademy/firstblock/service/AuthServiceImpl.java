package com.firstacademy.firstblock.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;

import com.firstacademy.firstblock.dto.model.UserDto;
import static com.firstacademy.firstblock.security.SecurityConstants.*;
import static com.firstacademy.firstblock.util.SecretKeyUtils.*;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication authenticate(UserDto userDto) throws AuthenticationException {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(userDto.getEmail(),
                userDto.getPassword());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        return authenticationResponse;
    }

    @Override
    public String generateJwt(List<String> roles) {
        String token = Jwts.builder().claims().add("roles", roles).and()
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(generateSecretKey(SECRET))
                .compact();
        return token;
    }

    @Override
    public void setJwtCookie(HttpServletResponse res, Authentication auth) {
        if (auth.getPrincipal() != null) {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth
                    .getPrincipal();
            String login = user.getUsername();
            if (login != null && login.length() > 0) {
                List<String> roles = new ArrayList<>();
                user.getAuthorities().stream().forEach(authority -> roles.add(authority.getAuthority()));
                String token = generateJwt(roles);

                res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
            }
        }
    }

}
