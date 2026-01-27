package com.lgambier.spaceagency.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static Jwt getCurrentJwt() {
        Authentication auth =
                SecurityContextHolder
                        .getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            return null;
        }
        return jwt;
    }

    public static String getJwtUserEmail(){
        Jwt currentJwt = getCurrentJwt();

        if(currentJwt != null){
            return currentJwt.getClaimAsString("space-agency-email");
        }else{
            throw new NullPointerException();
        }
    }
}
