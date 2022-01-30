package com.ambrosia.main.auth.jwtauthentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationResponse {
    private final String jwt;
    private final String firebaseToken;
    private final Long appUserId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String appUserRole;
}
