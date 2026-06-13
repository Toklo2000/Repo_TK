package com.tk_game.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(
          "/",
          "/index.html",
          "/register.html",
          "/create-character.html",
          "/static/**",
          "/js/**",
          "/css/**",
          "/api/auth/register"
        ).permitAll()
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginProcessingUrl("/login")
        .permitAll()
      )
      .httpBasic(basic -> basic.disable());
    return http.build();
  }
}
