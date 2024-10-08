package org.othertwink.employeeapp.security.config;

import org.othertwink.employeeapp.security.filter.JwtAuthenticationFilter;
import org.othertwink.employeeapp.security.filter.LoggingFilter;
import org.othertwink.employeeapp.security.service.OurUserDetailedService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final OurUserDetailedService ourUserDetailedService;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final LoggingFilter loggingFilter;

    public SecurityConfig(OurUserDetailedService ourUserDetailedService, JwtAuthenticationFilter jwtAuthenticationFilter, LoggingFilter loggingFilter) {
        this.ourUserDetailedService = ourUserDetailedService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.loggingFilter = loggingFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register","/home").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requiresChannel(channel -> channel.anyRequest().requiresSecure()) // HTTPS
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(loggingFilter, JwtAuthenticationFilter.class)
                .build();
    }
}
