package com.baddary.salesAPI.controller;

import com.baddary.salesAPI.dto.UserDTO;
import com.baddary.salesAPI.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<UserDTO> findByNameIgnoreCase(@RequestParam String name) {
        return userService.findByNameIgnoreCase(name)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    public long count() {
        return userService.count();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO dto = userService.addUser(userDTO);
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping("/first")
    public ResponseEntity<UserDTO> addUserFirst(@RequestBody @Valid UserDTO userDTO) {
        if (userService.count() > 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        UserDTO dto = userService.addUser(userDTO);
        return ResponseEntity.status(201).body(dto);
    }

}
