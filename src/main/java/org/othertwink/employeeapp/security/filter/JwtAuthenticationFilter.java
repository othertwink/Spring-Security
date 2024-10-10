package org.othertwink.employeeapp.security.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.othertwink.employeeapp.exception.auth.InvalidJwtException;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.security.util.JWTUtils;
import org.othertwink.employeeapp.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;



@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final EmployeeService employeeService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${app.security.max-failed-attempts}")
    private int MAX_FAILED_ATTEMPTS;

    // Метод, выполняемый для каждого HTTP запроса
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Шаг 1: Извлечение заголовка авторизации из запроса
        final String authorizationHeader = request.getHeader("Authorization");
        logger.debug("Starting JwtAuthenticationFilter");

        // Шаг 2: Проверка наличия заголовка авторизации

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            logger.warn("Empty header. Passing JwtAuthenticationFilter for request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Шаг 3: Извлечение токена из заголовка
            String token = authorizationHeader.substring(7);

            // Проверка истечения токена
            if (jwtUtils.isTokenExpired(token)) {
                logger.warn("JWT token has expired for request: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");
                return;
            }

            // Шаг 4: Извлечение имени пользователя из JWT токена
            String username = null;
            try {
                username = jwtUtils.extractUsername(token);
            } catch (JwtException e) {
                logger.error("Invalid JWT token: {}", e.getMessage());
                throw new InvalidJwtException(e.getMessage());
            }

            // Шаг 5: Проверка валидности токена и аутентификации
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = employeeService.loadUserByUsername(username);
                Employee employee = (Employee) userDetails;

                // Проверка на блокировку аккаунта
                if (!employee.isAccountNonLocked()) {
                    logger.warn("Account is locked for user: {}", username);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Account is locked");
                    return;
                }

                // Проверка валидности токена
                if (jwtUtils.isTokenValid(token, userDetails)) {
                    logger.info("Successful authentication for user: {}", username);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Шаг 6: Создание нового контекста безопасности
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // Увеличить количество неудачных попыток входа
                    employeeService.increaseFailedLoginAttempts(employee);
                    logger.warn("Invalid JWT token for user: {}. Incrementing failed attempts.", username);
                    // Если неудачных попыток больше 5 — блокируем пользователя
                    if (employee.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                        logger.warn("Account locked due to too many failed login attempts for user: {}", username);
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Account locked due to too many failed login attempts");
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
