package com.pm.authservice2dot0.service;

import com.pm.authservice2dot0.dto.LoginRequestDTO;
import com.pm.authservice2dot0.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    public AuthService(JwtUtil jwtUtil, UserService userService, PasswordEncoder passwordEncoder)

    {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
       Optional<String> token= userService.findByEmail(loginRequestDTO.getEmail())
               .filter(user -> passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword()))
               .map(u -> jwtUtil.generateToken(u.getEmail(),u.getRole()));

        return token;

    }

    public boolean validateToken(String token) {
        try{
            jwtUtil.validateToken(token);
            return true;
        }
        catch(JwtException e) {
            return false;
        }
    }
}
