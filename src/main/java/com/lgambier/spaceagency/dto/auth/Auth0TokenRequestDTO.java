package com.lgambier.spaceagency.dto.auth;

public record Auth0TokenRequestDTO(String grant_type, String username, String password, String audience, String scope, String client_id, String client_secret, String connection) {
}
