package com.lgambier.spaceagency.auth;

import com.lgambier.spaceagency.dto.auth.Auth0TokenDTO;
import com.lgambier.spaceagency.dto.auth.Auth0TokenRequestDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(value = "/oauth", accept = "application/json")
public interface AuthService {

    @PostExchange("/token")
    Auth0TokenDTO getToken(@RequestBody Auth0TokenRequestDTO auth0TokenRequestDTO);
}