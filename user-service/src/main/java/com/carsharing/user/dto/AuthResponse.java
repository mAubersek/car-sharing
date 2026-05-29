package com.carsharing.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    public String token;
    public String email;
    public String role;
}
