package com.luana.farm.controller;

import com.luana.farm.dto.JwtResponse;
import com.luana.farm.dto.LoginRequest;
import com.luana.farm.security.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Normally, you would authenticate the user here
        // For simplicity, we'll assume the login is successful
        String token = jwtTokenUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }

}

