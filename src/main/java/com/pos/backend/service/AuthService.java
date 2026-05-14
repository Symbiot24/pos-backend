package com.pos.backend.service;

import com.pos.backend.dto.request.LoginRequest;
import com.pos.backend.dto.request.SignupRequest;
import com.pos.backend.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse registerShopOwner(SignupRequest request);
    JwtResponse login(LoginRequest request);
}