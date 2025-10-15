package com.grewmeet.user.auth.controller;

import com.grewmeet.user.auth.domain.UserRole;
import com.grewmeet.user.auth.dto.PasswordUpdateRequest;
import com.grewmeet.user.auth.dto.LoginRequest;
import com.grewmeet.user.auth.dto.LoginResponse;
import com.grewmeet.user.auth.dto.UserRegisterRequest;
import com.grewmeet.user.auth.dto.UserRegisterResponse;
import com.grewmeet.user.auth.dto.UserSearchResponse;
import com.grewmeet.user.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/users/register")
    public ResponseEntity<UserRegisterResponse> registerUser(
            @Valid @RequestBody UserRegisterRequest request) {

        UserRegisterResponse response = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PutMapping("/users/{userId}/password")
    public ResponseEntity<Void> updatePassword( @PathVariable String userId,
                                                @Valid @RequestBody PasswordUpdateRequest request) {

        authService.updatePassword(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/auth")
    public ResponseEntity<Void> deleteAuthCredential(
            @PathVariable String userId) {

        authService.deleteAuthCredential(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserSearchResponse> getUserById(@PathVariable String userId) {

        UserSearchResponse response = authService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserSearchResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserSearchResponse> response = authService.getAllUsers(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<Page<UserSearchResponse>> getUsersByRole(@PathVariable UserRole role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserSearchResponse> response = authService.getUsersByRole(role, page, size);
        return ResponseEntity.ok(response);
    }
}
