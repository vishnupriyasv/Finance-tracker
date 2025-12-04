package com.vishnu.finance.finance_tracker.controller;

import com.vishnu.finance.finance_tracker.dto.AuthRequest;
import com.vishnu.finance.finance_tracker.dto.AuthResponse;
import com.vishnu.finance.finance_tracker.dto.UserDTO;
import com.vishnu.finance.finance_tracker.entity.User;
import com.vishnu.finance.finance_tracker.security.JwtUtil;
import com.vishnu.finance.finance_tracker.service.UserService;
import com.vishnu.finance.finance_tracker.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AuthRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }
        
        User user = userService.register(request.getUsername(), request.getEmail(), request.getPassword());
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());

        return ResponseEntity.status(201).body(Map.of("message", "User registered successfully", "user", userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            String token = jwtUtil.generateToken(username);
            
            User user = userRepository.findByUsername(username).orElseThrow();
            UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
            AuthResponse response = new AuthResponse(token, userDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(userDTO);
    }
}
