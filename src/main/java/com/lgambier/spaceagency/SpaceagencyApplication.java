package com.lgambier.spaceagency;

import com.lgambier.spaceagency.auth.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Locale;

@SpringBootApplication
public class SpaceagencyApplication {


    @Value("${AUTH0_BASE_URL}")
    private String auth0BaseUrl;

    static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(SpaceagencyApplication.class, args);
    }

    @Bean
    public AuthService authService(RestClient.Builder restClientBuilder) {
        RestClient client = restClientBuilder
                                    .baseUrl(auth0BaseUrl)
                                    .build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                                                  .builderFor(restClientAdapter)
                                                  .build();

        return factory.createClient(AuthService.class);
    }

}
