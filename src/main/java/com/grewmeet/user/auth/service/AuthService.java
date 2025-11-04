package com.grewmeet.user.auth.service;

import com.grewmeet.user.auth.dto.PasswordUpdateRequest;
import com.grewmeet.user.auth.dto.LoginRequest;
import com.grewmeet.user.auth.dto.LoginResponse;
import com.grewmeet.user.auth.dto.UserRegisterRequest;
import com.grewmeet.user.auth.dto.UserRegisterResponse;
import com.grewmeet.user.auth.dto.UserSearchResponse;
import com.grewmeet.user.auth.domain.UserRole;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface AuthService {

    UserRegisterResponse registerUser(@Valid UserRegisterRequest request);
    
    void updatePassword(String userId, @Valid PasswordUpdateRequest request);
    
    void deleteAuthCredential(String userId);
    
    UserSearchResponse getUserById(String userId);
    UserSearchResponse getUserByEmail(String email);
    
    Page<UserSearchResponse> getAllUsers(int page, int size);
    
    Page<UserSearchResponse> getUsersByRole(UserRole role, int page, int size);
    
    LoginResponse login(@Valid LoginRequest request);
    
}
