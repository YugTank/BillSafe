package com.billsafe.billsafe.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min=3, max=100)
    private String username;

    @NotBlank
    @Size(min=8, max=255)
    private String password;

    @Email
    @NotBlank
    @Size(max=255)
    private String email;
}
