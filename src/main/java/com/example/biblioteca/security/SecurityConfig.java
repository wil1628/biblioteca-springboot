package com.example.biblioteca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Configuración de UserDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // Configuración de BCryptPasswordEncore
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService uds) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Configuración de AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configuración de SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Reglas de autorización
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/nosotros", "/catalogo", "/contacto", "/auth/registro", "/css/**",
                                "/js/**", "/image/**")
                        .permitAll() // Acceso sin autorización
                        .requestMatchers("/catalogo/alquilar/**").authenticated() // Requiere autenticación
                        .requestMatchers("/admin/usuarios/**").hasRole("ADMIN") // Solo ADMIN puede acceder
                        .requestMatchers("/usuario/**").hasAnyRole("USER", "ADMIN") // Solo USER y ADMIN pueden acceder
                        .requestMatchers("/libros/**", "/categorias/**", "/autores/**", "/usuarios/**", "/prestamos/**", "/ejemplares/**").hasRole("ADMIN") // Solo ADMIN puede acceder
                        .anyRequest().authenticated()) // Requiere autenticación
                .formLogin(form -> form // Formulario de inicio de sesión
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/auth/default", true)
                        .permitAll())
                .logout(logout -> logout // Cierra sesión
                        .logoutSuccessUrl("/")
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/auth/acceso-denegado")); // Página de acceso denegado

        return http.build();
    }

}
