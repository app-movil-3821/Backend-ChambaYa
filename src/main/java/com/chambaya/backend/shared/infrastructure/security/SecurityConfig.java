package com.chambaya.backend.shared.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.chambaya.backend.iam.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf ->csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/jobs").hasRole("CONTRATANTE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/jobs/**").hasRole("CONTRATANTE")

                        .requestMatchers(HttpMethod.POST, "/api/v1/enrollments").hasRole("CHAMBEADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/enrollments/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/v1/**").authenticated()

                        .requestMatchers(HttpMethod.PUT, "/api/v1/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/communications/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/communications/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/payments/**").hasRole("CONTRATANTE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/payments/**").hasRole("CONTRATANTE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/favorites/**").hasRole("CHAMBEADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/favorites/**").hasRole("CHAMBEADOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
