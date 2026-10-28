package com.baddary.salesAPI.controller;

import java.util.Map;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baddary.salesAPI.dto.LoginRequestDTO;
import com.baddary.salesAPI.dto.LoginResponseDTO;
import com.baddary.salesAPI.dto.UserDTO;
import com.baddary.salesAPI.service.UserService;
import com.baddary.salesAPI.util.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private static final String DUMMY_BCRYPT_HASH = "$2a$10$7EqJtq98hPqEX7fNZaFWoO.HdwvfS0G1gXvE8j7z7v7v7v7v7v7v7";


    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        // Find user by username (case-insensitive)
        Optional<UserDTO> userOpt = userService.findByNameIgnoreCase(loginRequest.getUsername());
        String storedPassword = userOpt.map(UserDTO::getPassword).orElse(DUMMY_BCRYPT_HASH);
        boolean passwordMatches = BCrypt.checkpw(loginRequest.getPassword(), storedPassword);

        if (userOpt.isEmpty() || !passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid username or password"));
        }
        UserDTO user = userOpt.get();
        String token = jwtUtil.generateToken(user.getName(), user.getRole().name());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(user.getId());
        response.setUserName(user.getName());
        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}
