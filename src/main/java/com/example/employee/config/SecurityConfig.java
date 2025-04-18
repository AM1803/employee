package com.example.employee.config;

import com.example.employee.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
        //below line creates and returns a singleton instance of NoOpPassword
        return NoOpPasswordEncoder.getInstance();
        //this is used for comparison of plain text password after decryption
        //not for actual encoding for storage
        //after jasypt decryption in CustomUserDetailsService Spring security needs to compare the two plain text password done by this
   }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
       //below line gets the AuthentictionManager from the configuration return it as spring managed bean
        return authenticationConfiguration.getAuthenticationManager();
        //actual process to verify user credentials
        //this is the central component of spring security that handles the actual process of user verification
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

                                // Role: USER - Only View (GET) access
                                .requestMatchers(HttpMethod.GET, "/employees/**").hasRole("USER")

                                // Role: DEVELOPER - Create (POST) and Update (PUT) access
                                .requestMatchers(HttpMethod.POST, "/employees").hasRole("DEVELOPER")
                                .requestMatchers(HttpMethod.PUT, "/employees/**").hasRole("DEVELOPER")

                                // Role: ADMIN - Only Delete (DELETE) access
                                .requestMatchers(HttpMethod.DELETE, "/employees/**").hasRole("ADMIN")

                                .anyRequest().authenticated()
                        //tells all other requests to your app require authentication
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //configures how sessions are handled
                //Stateless means the app won't relly on server side sessions to tracked logged in users
                //JWT handles client side
                .httpBasic(Customizer.withDefaults())
                //enables basic auth for testing purpose
                .addFilterBefore(jwtRequestFilter
                        , UsernamePasswordAuthenticationFilter.class);
        //tells spring security to run jwtRequestFilter before the standard username and pwd auth filter
        //this means it will check the JWT token first

        return http.build();
    }
}
//Flow is in a club bouncer is there seeing the annotations command is to secure the website
//it has a scanner jwtrequestfilter that scans id to verify the jwt tokens
//this also has a way to check user info like a guest list UserDetailsService
//password encoder to check the passwords entered to the passwords in the stored file
//authentication manager is the main thing that verifies id this is set up for knowing how to use our userdetailsservice and passwordencoder to verify user
//securoty filter chain is where all the rules are written about who can access what parts of our website
//auth is where we set access rules for different web urls
//.request matchers line says that register and login pages are open to everyone
//authenticated says that for all the websites other than this we need to be logged in
//session tells that no log is going to be kept in the server side instead jwt token will be used
//addfilter before is there to say that jwt toekn is checked first before the basic username and password are used
