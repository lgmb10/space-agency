package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.auth.AuthService;
import com.lgambier.spaceagency.config.AbstractIntegrationTest;
import com.lgambier.spaceagency.dto.auth.Auth0TokenDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class SecurityTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void loginUser_whenInvalidCredentials_shouldReturn401() throws Exception {
        when(authService.getToken(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        JSONObject authBody = new JSONObject();
        authBody.put("login", "invalid");
        authBody.put("password", "invalid");

        mockMvc
                .perform(post("/api/auth/login")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(authBody.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginUser_whenValidCredentials_shouldReturn200() throws Exception {
        when(authService.getToken(any())).thenReturn(
                new Auth0TokenDTO("test_token", "id", "scope", "123456789", "Bearer"));

        JSONObject authBody = new JSONObject();
        authBody.put("login", "valid");
        authBody.put("password", "valid");

        mockMvc
                .perform(post("/api/auth/login")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(authBody.toString()))
                .andExpect(status().isOk());
    }
}
