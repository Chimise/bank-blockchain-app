package com.firstacademy.controllers;

import com.firstacademy.entity.User;
import com.firstacademy.repository.UserRepository;
import com.firstacademy.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final String jwt = jwtUtil.generateToken(authenticationRequest.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    // @PostMapping("/register")
    // public ResponseEntity<?> registerUser(@RequestBody User user) {
    //     user.setPassword(passwordEncoder.encode(user.getPassword()));
    //     userRepository.save(user);
    //     return ResponseEntity.ok("User registered successfully");
    // }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
