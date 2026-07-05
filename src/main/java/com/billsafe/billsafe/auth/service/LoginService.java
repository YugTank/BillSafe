package com.billsafe.billsafe.auth.service;

import com.billsafe.billsafe.auth.dto.LoginRequest;
import com.billsafe.billsafe.auth.dto.LoginResponse;
import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest loginRequest) {
        String email=loginRequest.getEmail().trim().toLowerCase();
        String password=loginRequest.getPassword();

        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Invalid credentials"));
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw  new RuntimeException("Invalid credentials");
        }
        String token= jwtService.generateToken(email);
        return new LoginResponse(token);
    }
}
