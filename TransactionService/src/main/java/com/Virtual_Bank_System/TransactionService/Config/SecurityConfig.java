package com.Virtual_Bank_System.TransactionService.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())                       // DISABLE CSRF completely
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // allow all
                .httpBasic(httpBasic -> httpBasic.disable())       // disable HTTP Basic
                .formLogin(form -> form.disable());                // disable login form
        return http.build();
    }
}