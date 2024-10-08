package org.othertwink.employeeapp.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.othertwink.employeeapp.security.util.AuthenticationLogData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("Request: {} {} from IP: {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

        try {
            filterChain.doFilter(request, response);
        } finally {
            AuthenticationLogData logData = (AuthenticationLogData) request.getAttribute("authLogData");

            if (logData != null) {
                if ("successful".equals(logData.getStatus())) {
                    logger.info("Successful authentication for user: {}", logData.getUsername());
                } else if ("failed".equals(logData.getStatus())) {
                    logger.warn("Failed authentication: reason - {}", logData.getReason());
                }
            }

            logger.info("Response: {} for {} {}", response.getStatus(), request.getMethod(), request.getRequestURI());
        }
        filterChain.doFilter(request, response);
    }
}
