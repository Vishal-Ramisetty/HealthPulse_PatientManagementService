package com.pm.authservice2dot0.controller;

import com.pm.authservice2dot0.dto.LoginRequestDTO;
import com.pm.authservice2dot0.dto.LoginResponseDTO;
import com.pm.authservice2dot0.service.AuthService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@RestController
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    /**
     * Handles user login requests.
     *
     * @return A string with token value
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<String> token=authService.authenticate(loginRequestDTO);
        if(token.isPresent()){
            return ResponseEntity.ok(new LoginResponseDTO(token.get()));
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO("Invalid credentials"));
        }

    }

}
