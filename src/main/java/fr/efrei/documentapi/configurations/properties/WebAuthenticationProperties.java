package fr.efrei.documentapi.configurations.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("authentication")
public class WebAuthenticationProperties {

    private String issuerUrl;

    private String publicKeyUri;
}
