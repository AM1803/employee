package com.example.employee.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;
//java utility class
//to generate and validate utilities
@Component
public class JwtUtil {

    private final String secret = "1001"; // Used to sign and verify jwt
    private final long validityTime = 3600000;
    // 1 hour in milliseconds will be the validity of a generated jwt

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + validityTime))
                .withClaim("roles", userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(secret));
    }
   // Creates a new JWT for the given user details.
   // It includes the username as the subject, the issue and expiration times, and the user's roles.
   // It then signs the JWT using the secret key and the HMAC256 algorithm.
    public String getUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getSubject();
    }
  //  Extracts the username (subject) from a given JWT.
  //  It verifies the token's signature using the secret key.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            String username = verifier.verify(token).getSubject();
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JWTVerificationException e) {
            return false;
        }
    }
    //Checks if a given JWT is valid for a specific user.
    //It verifies the signature,
    //checks if the username in the token matches the user's username, and if the token has not expired.
    private boolean isTokenExpired(String token) {
        Date expirationDate = JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getExpiresAt();
        return expirationDate.before(new Date());
    }
   // Checks if a given JWT has expired by comparing its expiration time with the current time.
}