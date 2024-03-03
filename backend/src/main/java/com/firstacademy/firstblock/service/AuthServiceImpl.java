package com.firstacademy.firstblock.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import jakarta.servlet.http.HttpServletResponse;

import com.firstacademy.firstblock.dto.model.UserDto;
import static com.firstacademy.firstblock.security.SecurityConstants.*;
import static com.firstacademy.firstblock.util.SecretKeyUtils.*;
import com.firstacademy.firstblock.util.CookieUtils;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication authenticate(String email, String password) throws AuthenticationException {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(email,
                password);
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        return authenticationResponse;
    }

    @Override
    public String generateJwt(String username, List<String> roles) {
        String token = Jwts.builder().claims().add("roles", roles).subject(username).and()
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(generateSecretKey(SECRET))
                .compact();
        return token;
    }

    @Override
    public String setJwtCookie(HttpServletResponse res, Authentication auth) {
        if (auth.getPrincipal() != null) {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth
                    .getPrincipal();
            String login = user.getUsername();
            if (login != null && login.length() > 0) {
                List<String> roles = new ArrayList<>();
                user.getAuthorities().stream().forEach(authority -> roles.add(authority.getAuthority()));
                String token = generateJwt(login, roles);

                res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
                return token;
            }
        }

        return null;
    }

}
