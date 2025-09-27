package com.example.usertaskmanager.controller;

import com.example.usertaskmanager.dto.UserLoginDto;
import com.example.usertaskmanager.dto.UserRegistrationDto;
import com.example.usertaskmanager.service.AuthService;
import com.example.usertaskmanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;  // inject AuthService

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody @Valid UserRegistrationDto dto) {
        userService.registerUser(dto);
        Map<String,String> resp = Map.of("message", "User registered successfully!");
        return ResponseEntity.status(201).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid UserLoginDto dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok(Map.of("token", token));
    }
}


