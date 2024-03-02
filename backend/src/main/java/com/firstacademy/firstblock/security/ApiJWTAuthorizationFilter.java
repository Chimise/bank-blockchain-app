package com.firstacademy.firstblock.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.firstacademy.firstblock.security.SecurityConstants.*;
import static com.firstacademy.firstblock.util.SecretKeyUtils.*;
import com.firstacademy.firstblock.util.CookieUtils;

public class ApiJWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        String token = CookieUtils.extractJwtToken(req, COOKIE_AUTH_NAME);
        if (token == null) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
        if (authentication != null) {
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (token != null) {
            Claims claims = (Claims) Jwts.parser()
                    .verifyWith(generateSecretKey(SECRET))
                    .build().parse(token.replace(TOKEN_PREFIX, "")).getPayload();
            // Extract the UserName
            String user = claims.getSubject();
            // Extract the Roles
            ArrayList<String> roles = (ArrayList<String>) claims.get("roles");
            // Then convert Roles to GrantedAuthority Object for injecting
            ArrayList<GrantedAuthority> list = new ArrayList<>();
            if (roles != null) {
                for (String a : roles) {
                    GrantedAuthority g = new SimpleGrantedAuthority(a);
                    list.add(g);
                }
            }
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, list);
            }
            return null;
        }
        return null;
    }

}
