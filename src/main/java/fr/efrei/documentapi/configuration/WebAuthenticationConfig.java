package fr.efrei.documentapi.configuration;

import fr.efrei.documentapi.configuration.properties.WebAuthenticationProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebAuthenticationConfig {

    WebAuthenticationProperties authenticationProperties;

    public WebAuthenticationConfig(
            WebAuthenticationProperties webAuthenticationProperties
    ) {
        this.authenticationProperties = webAuthenticationProperties;
    }

    @Bean
    @Qualifier("authenticationClient")
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(authenticationProperties.getIssuerUrl()).build();
    }
}
