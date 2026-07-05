package com.billsafe.billsafe.auth.controller;

import com.billsafe.billsafe.auth.dto.LoginRequest;
import com.billsafe.billsafe.auth.dto.LoginResponse;
import com.billsafe.billsafe.auth.dto.RegisterRequest;
import com.billsafe.billsafe.auth.dto.RegisterResponse;
import com.billsafe.billsafe.auth.service.LoginService;
import com.billsafe.billsafe.auth.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterService registerService;
    private final LoginService loginService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return registerService.register(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }


}
