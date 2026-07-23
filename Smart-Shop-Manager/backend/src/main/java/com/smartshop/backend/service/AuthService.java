package com.smartshop.backend.service;

import com.smartshop.backend.dto.LoginRequest;
import com.smartshop.backend.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}