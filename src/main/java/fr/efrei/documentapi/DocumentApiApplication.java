package fr.efrei.documentapi;

import fr.efrei.documentapi.configurations.properties.WebAuthenticationProperties;
import fr.efrei.documentapi.configurations.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class, WebAuthenticationProperties.class})
@EnableCaching
public class DocumentApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentApiApplication.class, args);
	}

}
