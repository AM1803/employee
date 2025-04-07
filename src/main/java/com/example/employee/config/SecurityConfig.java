package com.example.employee.config;

import com.example.employee.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;
//this class configures how this application is secured
@Configuration//this class provides configuration for the spring app
@EnableWebSecurity//Enables spring security web security features
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {//to encode password
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
        //actual process to verify user credentials
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //where you define security rules for applications endpoints
        http
                .csrf(csrf -> csrf.disable())
                //disables cross site request forgery
                .authorizeHttpRequests(auth -> auth
                        //configures which requests require are allowed and which require authentication
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        //allows anyone to access login and register without logging in
                        .anyRequest().authenticated()
                        //tells all other requests to your app require authentication
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               //configures how sessions are handled
                //Stateless means the app won't relly on server side sessions to tracked logged in users
                //JWT handles client side
                .httpBasic(Customizer.withDefaults())
                //enables basic auth for testing purpose
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        //tells spring security to run jwtRequestFilter before the standard username and pwd auth filter
        //this means it will check the JWT token first

        return http.build();
    }
}