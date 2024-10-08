//package controller;
//
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@TestConfiguration
//@EnableWebSecurity
//public class TestSecurityConfig{
//
//    @Bean
//    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/register", "/home").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .requiresChannel(channel -> channel.anyRequest().requiresInsecure()) // Не требует HTTPS
//                .build();
//    }
//}
