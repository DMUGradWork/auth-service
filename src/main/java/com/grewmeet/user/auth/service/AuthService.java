package com.grewmeet.user.auth.service;

import com.grewmeet.user.auth.dto.RegisterRequest;
import com.grewmeet.user.auth.dto.RegisterResponse;
import jakarta.validation.Valid;

public interface AuthService {
    RegisterResponse register(@Valid RegisterRequest request);
}
