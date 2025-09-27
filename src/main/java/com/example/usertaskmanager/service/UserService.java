package com.example.usertaskmanager.service;

import com.example.usertaskmanager.dto.UserRegistrationDto;
import com.example.usertaskmanager.exception.UserAlreadyExistsException;
import com.example.usertaskmanager.model.User;
import com.example.usertaskmanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegistrationDto dto) {
        logger.info("Registration attempt for username: {}", dto.getUsername());

        if (userRepository.existsByUsername(dto.getUsername())) {
            logger.warn("Registration failed: username already taken: {}", dto.getUsername());
            throw new UserAlreadyExistsException("Username already taken");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            logger.warn("Registration failed: email already registered: {}", dto.getEmail());
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = new User(dto.getUsername(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(user);

        logger.info("User registered successfully: {}", savedUser.getUsername());
        return savedUser;
    }
}
