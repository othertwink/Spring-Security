package org.othertwink.employeeapp.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.othertwink.employeeapp.model.entity.User;
import org.othertwink.employeeapp.security.service.OurUserDetailedService;
import org.othertwink.employeeapp.security.util.AuthenticationLogData;
import org.othertwink.employeeapp.security.util.JWTUtils;
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
    private final OurUserDetailedService ourUserDetailedService;
    private final LoggingFilter loggingFilter;

    // Метод, выполняемый для каждого HTTP запроса
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Шаг 1: Извлечение заголовка авторизации из запроса
        final String authorizationHeader = request.getHeader("Authorization");

        // Создаем объект для логирования данных аутентификации
        AuthenticationLogData logData = new AuthenticationLogData();

        // Шаг 2: Проверка наличия заголовка авторизации
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Шаг 3: Извлечение токена из заголовка
            String token = authorizationHeader.substring(7);

            // Проверка срока действия токена
            if (jwtUtils.isTokenExpired(token)) {
                logData.setStatus("failed");
                logData.setReason("Token expired");
                request.setAttribute("authLogData", logData);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");
                return;
            }

            // Шаг 4: Извлечение имени пользователя из JWT токена
            String username = jwtUtils.extractUsername(token);
            logData.setUsername(username);

            // Шаг 5: Проверка валидности токена и аутентификации
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = ourUserDetailedService.loadUserByUsername(username);
                User user = (User) userDetails;

                // Проверка на блокировку аккаунта
                if (!user.isAccountNonLocked()) {
                    logData.setStatus("failed");
                    logData.setReason("Account locked");
                    request.setAttribute("authLogData", logData);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Account is locked");
                    return;
                }

                // Проверка валидности токена
                if (jwtUtils.isTokenValid(token, userDetails)) {
                    // Создание объекта Authentication
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Шаг 6: Создание нового контекста безопасности
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logData.setStatus("successful");
                } else {
                    // Увеличить количество неудачных попыток входа
                    ourUserDetailedService.increaseFailedLoginAttempts(user);
                    logData.setStatus("failed");
                    logData.setReason("Invalid token or too many failed attempts");

                    // Если неудачных попыток больше 5 — блокируем пользователя
                    if (user.getFailedLoginAttempts() >= 5) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Account locked due to too many failed login attempts");
                        return;
                    }
                }
            }
        }

        request.setAttribute("authLogData", logData);
        filterChain.doFilter(request, response);
    }
}
