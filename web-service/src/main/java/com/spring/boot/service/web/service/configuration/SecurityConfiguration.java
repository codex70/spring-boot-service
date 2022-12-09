package com.spring.boot.service.web.service.configuration;

import com.spring.boot.service.web.service.properties.ActuatorProperties;
import com.spring.boot.service.web.service.properties.SecuredPathsProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration
{

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    private final ActuatorProperties actuatorProperties;

    @Bean
    // We are expecting to use this deprecated method, that's how in memory works. Not ideal for production though
    @SuppressWarnings ("java:S1874")
    public InMemoryUserDetailsManager inMemoryUserDetailsManager()
    {
        final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User.withDefaultPasswordEncoder()
                               .username(actuatorProperties.getName())
                               .password(actuatorProperties.getPassword())
                               .roles(actuatorProperties.getRoles())
                               .build());
        logger.info("Imported user {}", actuatorProperties);

        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http, SecuredPathsProperties securedPathsProperties ) throws Exception
    {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .cors()
                .and()
                .securityMatcher(antMatcher("/spring-boot-service/**"))
                .authorizeHttpRequests(authz -> authz
                                               .requestMatchers(securedPathsProperties.getPermitAll()).permitAll()
                                               .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                                               .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                                               .requestMatchers("/spring-boot-service/**").permitAll()
                                               .anyRequest().authenticated()
                                      )
                .formLogin()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }


    /**
     * customizing the cors configuration to allow resource sharing with specific origins
     */
    @Profile ({"prod", "staging", "test", "dev"})
    @Bean
    CorsConfigurationSource corsConfigurationSource( )
    {
        CorsConfiguration configuration = new CorsConfiguration();

        List<String> allowedOrigins = new ArrayList<>();

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.toString(), HttpMethod.POST.toString()));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

