package com.example.employee.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//intercepts all incoming http request to check for jwt in authorisation header
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
//ensures that filter runs once per request
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//this method is done for each request
        final String authorizationHeader = request.getHeader("Authorization");
//gets authorization header
        String username = null;
        String jwt = null;

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            //if header starts with bearer it extracts jwt
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.getUsernameFromToken(jwt);
            //to get username from jwt
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //if user is found but not authenticated
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            //loads user details from userDetailsService
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                //it uses to check if jwt is valid for user
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                //which represents an authenticated user in spring security
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }//this then sets SecurityContextHolder for telling that now authenticated
        }
        filterChain.doFilter(request, response);
        //to pass the request to the next filter in the chain or your controller
    }
}