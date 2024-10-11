package org.othertwink.employeeapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.model.entity.enums.Role;
import org.othertwink.employeeapp.repository.EmployeeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GitHubOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final EmployeeRepo employeeRepo;

    private static final Logger logger = LoggerFactory.getLogger(GitHubOAuth2Service.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String username = oAuth2User.getAttribute("login");
        String email = oAuth2User.getAttribute("email");




        Employee employee = employeeRepo.findByUsername(username)
                .orElseGet(() -> {
                    Employee newEmployee = Employee.builder()
                            .username(username)
                            .firstName(oAuth2User.getAttribute("name"))
                            .email(email)
                            .password("N/A")
                            .role(Role.ADMIN) // поставил ADMIN для теста
                            .isAccountNonLocked(true)
                            .build();
                    return employeeRepo.save(newEmployee);
                });

        // 401 если лок
        if (!employee.isAccountNonLocked()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("account_locked"), "User account is locked.");
        }

        // Логирование успешной аутентификации
        logger.info("User '{}' successfully authenticated", username);

        Set<GrantedAuthority> authorities = new HashSet<>(employee.getAuthorities());
        if (employee.getRole() == Role.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "login");
    }

    public void revokeAccess(String username) {
        employeeRepo.findByUsername(username).ifPresent(employee -> {
            employee.setAccountNonLocked(false);
            employeeRepo.save(employee);
            logger.info("Access revoked for user '{}'", username);
        });
    }
}

