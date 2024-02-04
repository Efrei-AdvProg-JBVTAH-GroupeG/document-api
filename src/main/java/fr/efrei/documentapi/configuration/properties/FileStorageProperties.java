package fr.efrei.documentapi.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "document")
public class FileStorageProperties {
    private String uploadDir;
}

