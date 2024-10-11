package org.othertwink.employeeapp.security.config;

import lombok.RequiredArgsConstructor;
import org.othertwink.employeeapp.service.EmployeeService;
import org.othertwink.employeeapp.service.impl.GitHubOAuth2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final EmployeeService employeeService;


    private final GitHubOAuth2Service gitHubOAuth2Service;

    private static final Logger logger = LoggerFactory.getLogger(GitHubOAuth2Service.class);

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .csrf(CsrfConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/auth/**", "/login", "/error").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                    .loginPage("/login") // Указываем страницу для входа
                    .userInfoEndpoint(userInfo -> userInfo
                            .userService(gitHubOAuth2Service) // Указываем кастомный OAuth2UserService
                    )
                    .defaultSuccessUrl("/user") // Перенаправление после успешной аутентификации
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .logout(logout -> logout
                    .logoutUrl("/logout") // URL для выхода из системы
                    .logoutSuccessUrl("/") // Перенаправление на главную страницу после выхода
                    .invalidateHttpSession(true) // Удалить сессию
                    .deleteCookies("JSESSIONID") // Удалить cookies
                    .logoutSuccessHandler((request, response, authentication) -> {
                        String username = authentication.getName();
                        logger.info("User '{}' logged out successfully", username);
                        response.sendRedirect("/");
                    })
            )
            .build();
}


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // Установка сервиса для загрузки пользовательских данных
        daoAuthenticationProvider.setUserDetailsService(employeeService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
