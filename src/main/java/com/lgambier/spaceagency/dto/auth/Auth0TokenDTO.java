package com.lgambier.spaceagency.dto.auth;

public record Auth0TokenDTO(String access_token, String id_token, String scope, String expires_in, String token_type) {
}
