package com.billsafe.billsafe.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Email
    @Size(max=255)
    private String email;

    @NotBlank
    @Size(min=8, max=255)
    private String password;
}
