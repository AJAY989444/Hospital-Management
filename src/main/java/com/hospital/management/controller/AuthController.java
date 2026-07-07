package com.hospital.management.controller;

import com.hospital.management.dto.AuthRequest;
import com.hospital.management.dto.AuthResponse;
import com.hospital.management.dto.RegisterRequest;
import com.hospital.management.dto.UserDTO;
import com.hospital.management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration, login, token refresh, and logout")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user profile as a PATIENT or DOCTOR based on role fields.")
    @ApiResponse(responseCode = "201", description = "User successfully registered")
    @ApiResponse(responseCode = "400", description = "Invalid request payload or email duplication")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO user = userService.registerUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user credentials", description = "Validates user login details, returns JWT access and refresh tokens, and saves access token in an HTTP-only cookie.")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
    @ApiResponse(responseCode = "401", description = "Invalid email or password credentials")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest request, 
            HttpServletResponse response) {
        
        AuthResponse authResponse = userService.login(request);

        // Write JWT to HTTP-only cookie for server-side Thymeleaf page requests
        Cookie cookie = new Cookie("jwt_access_token", authResponse.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production if running over HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(900); // 15 mins matching JWT expiration
        response.addCookie(cookie);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh expired access token", description = "Validates refresh token to issue a new access token.")
    @ApiResponse(responseCode = "200", description = "Access token refreshed successfully")
    @ApiResponse(responseCode = "403", description = "Refresh token expired or invalid")
    public ResponseEntity<AuthResponse> refresh(
            @RequestParam("token") String token, 
            HttpServletResponse response) {
        
        AuthResponse authResponse = userService.refreshAccessToken(token);

        // Update cookie with new access token
        Cookie cookie = new Cookie("jwt_access_token", authResponse.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(900);
        response.addCookie(cookie);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "Log out user", description = "Invalidates the user session by clearing the JWT access cookie.")
    @ApiResponse(responseCode = "200", description = "Logged out successfully")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear HTTP-Only access token cookie
        Cookie cookie = new Cookie("jwt_access_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Immediately delete cookie
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
