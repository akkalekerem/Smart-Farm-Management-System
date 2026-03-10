package com.smartfarm.smartfarmmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Şifreleri güvenli bir şekilde hash'lemek için BCrypt kullanıyoruz
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Geliştirme aşamasında kolaylık için
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll() // Bu sayfalara herkes girebilir
                        .anyRequest().authenticated() // Diğer tüm sayfalar (ana panel dahil) giriş gerektirir
                )
                .formLogin(form -> form
                        .loginPage("/login") // Kendi giriş sayfamızı kullanacağımızı belirtiyoruz
                        .defaultSuccessUrl("/", true) // Giriş başarılıysa ana sayfaya yönlendir
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}