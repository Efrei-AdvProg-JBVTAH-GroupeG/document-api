package fr.efrei.documentapi.service.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "file")
public class FileStorageTestProperties {
    private String uploadDir;
}

