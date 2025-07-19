package com.pm.authservice2dot0.controller;

import com.pm.authservice2dot0.dto.LoginRequestDTO;
import com.pm.authservice2dot0.dto.LoginResponseDTO;
import com.pm.authservice2dot0.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@RestController
@Tag(name="Auth", description = "API for Authentication")
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
    @Operation(summary="User Login API")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<String> token=authService.authenticate(loginRequestDTO);
        if(token.isPresent()){
            return ResponseEntity.ok(new LoginResponseDTO(token.get()));
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO("Invalid credentials"));
        }

    }

    @Operation(summary = "Validate JWT Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateJWT(@RequestHeader ("Authorization") String authToken){
        if(authToken == null||!authToken.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        /*if(! authService.validateToken(authToken.substring(7)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        else
            return ResponseEntity.ok().build();*/
        return authService.validateToken(authToken.substring(7))?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
