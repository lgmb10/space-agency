package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.auth.AuthService;
import com.lgambier.spaceagency.dto.auth.Auth0TokenDTO;
import com.lgambier.spaceagency.dto.auth.Auth0TokenRequestDTO;
import com.lgambier.spaceagency.dto.auth.AuthRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${AUTH0_AUDIENCE}")
    private String audience;

    @Value("${AUTH0_CLIENT_ID}")
    private String clientId;

    @Value("${AUTH0_CLIENT_SECRET}")
    private String clientSecret;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequestDTO body) {
        Auth0TokenRequestDTO request = new Auth0TokenRequestDTO("password", body.login(), body.password(), audience,
                                                                "openid profile email", clientId, clientSecret,
                                                                "Username-Password-Authentication");

        Auth0TokenDTO response = authService.getToken(request);
        return ResponseEntity.ok(response.access_token());
    }
}
