package com.example.employee.controller;

import com.example.employee.security.JwtUtil;
import com.example.employee.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
//handles /auth/login requests
@RestController
@RequestMapping("/auth")
//marks this as controller that handles request under auth path
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    //used to perform actual authentication of the user
    @Autowired
    private CustomUserDetailsService userDetailsService;
//Used to load user details form db
    @Autowired
    private JwtUtil jwtUtil;
//utility class to generate JWT
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> credentials) throws Exception {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        }
        catch (BadCredentialsException ex) {
            throw ex; // In global exception handler to give 401 for invalid credentials
        } catch (DisabledException ex) {
            throw ex; //In GlobalExceptionHandler for diqabled accounts trying to log in
        } catch (LockedException ex) {
            throw ex; // In GlobalExceptionHandler for locked accounts
        } catch (Exception ex) {
            throw ex; // Let GlobalExceptionHandler handle this
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(Map.of("token", jwt));
    }
}