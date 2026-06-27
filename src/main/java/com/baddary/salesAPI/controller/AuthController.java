package com.baddary.salesAPI.controller;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
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

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        // 1. Find user by username (case-insensitive)
        Optional<UserDTO> userOpt = userService.findByNameIgnoreCase(loginRequest.getUsername());
        if (userOpt.isEmpty()) {
            // User not found – return 401 (Unauthorized)
            throw new RuntimeException("User name or password is incorrect");
        }

        UserDTO user = userOpt.get();

        // 2. Verify password (raw from request vs stored hash)
        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("User name or password is incorrect");
        }

        // 3. Generate a token (for now, a simple UUID; replace with JWT later)
        String token = jwtUtil.generateToken(user.getName(), user.getRole().name());

        // 4. Build response DTO
        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(user.getId());
        response.setUserName(user.getName());
        response.setToken(token);

        // 5. Return 200 OK with the token and user info
        return ResponseEntity.ok(response);
    }
}
