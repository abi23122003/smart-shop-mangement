package com.smartshop.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.smartshop.backend.dto.LoginRequest;
import com.smartshop.backend.dto.LoginResponse;
import com.smartshop.backend.security.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

  @Override
public LoginResponse login(LoginRequest request) {

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
    );

    String token = jwtService.generateToken(request.getUsername());

    return new LoginResponse(token);
}
}