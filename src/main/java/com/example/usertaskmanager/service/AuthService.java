package com.example.usertaskmanager.service;

import com.example.usertaskmanager.dto.UserLoginDto;
import com.example.usertaskmanager.exception.InvalidCredentialsException;
import com.example.usertaskmanager.model.User;
import com.example.usertaskmanager.repository.UserRepository;
import com.example.usertaskmanager.config.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(UserLoginDto dto) {
        logger.info("Login attempt for username: {}", dto.getUsername());

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> {
                    logger.warn("Login failed: username not found: {}", dto.getUsername());
                    return new InvalidCredentialsException("Invalid username or password");
                });

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            logger.warn("Login failed: invalid password for username: {}", dto.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }

        logger.info("User logged in successfully: {}", dto.getUsername());
        return jwtUtil.generateToken(user.getId(), user.getUsername());
    }
}
