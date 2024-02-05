package fr.efrei.documentapi.configuration;

import fr.efrei.documentapi.security.filter.AuthTokenFilter;
import fr.efrei.documentapi.service.TokenService;
import fr.efrei.documentapi.service.impl.JwtTokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig {
    private TokenService tokenService;

    public WebSecurityConfig(
            JwtTokenServiceImpl tokenService
    ){
        this.tokenService = tokenService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((auth) ->
                    auth
                            .requestMatchers(
                                    "/file/*",
                                    "/listFiles"
                            ).authenticated()
                            .requestMatchers(
                                    HttpMethod.POST,
                                    "/uploadFile"
                            ).hasRole("STUDENT")
                            .requestMatchers(
                                    HttpMethod.DELETE,
                                    "/deleteFile/*"
                            ).hasRole("STUDENT")
                            .requestMatchers(
                                    HttpMethod.GET,
                                    "/swagger-ui.html",
                                    "/swagger-ui/*",
                                    "/api-docs/swagger-config",
                                    "/api-docs"
                            ).permitAll()
                )
                .cors(Customizer.withDefaults())
                //csrf protection not needed as token base application
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter(tokenService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
