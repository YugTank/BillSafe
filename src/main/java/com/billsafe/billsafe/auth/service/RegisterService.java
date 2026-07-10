package com.billsafe.billsafe.auth.service;

import com.billsafe.billsafe.auth.dto.RegisterRequest;
import com.billsafe.billsafe.auth.dto.RegisterResponse;
import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.auth.repository.UserRepository;
import com.billsafe.billsafe.common.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegisterResponse register(RegisterRequest registerRequest) {
        String email=registerRequest.getEmail().trim().toLowerCase();
        String username=registerRequest.getUsername();
        String password=registerRequest.getPassword();

        if(userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistException("User already exists");
        }

        User user= User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        userRepository.save(user);
        return new RegisterResponse("User registered successfully");
    }
}
