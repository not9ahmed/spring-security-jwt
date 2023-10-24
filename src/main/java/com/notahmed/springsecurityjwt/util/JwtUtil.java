package com.notahmed.springsecurityjwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// TODO Write the methods here on 24/10/2023
@Service
public class JwtUtil {

    // TODO read JJWT docs for the required methods
    // will have boilerplate for JWT
    // generate token with jjwt
    // validate token
    // extractUserName, claims, etc

    private final String SECRET_KEY = "secret";

    private final SecretKey KEY = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(SECRET_KEY));


    /**
     * method to extract the username from a JWT token
     * @param token of type String
     * @return username from the token
     */
    private String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }


    /**
     * method to extract the expiration date from a token string
     * @param token of type string
     * @return data of the expiration from a token
     */
    private Date extractExpirationDate(String token) {

        return null;
    }



    // TODO check Claims from the library
    /**
     * method which extract a single claim from all claims in a JWT token
     * @param token
     * @param claimsResolver
     * @return a single claim
     * @param <T>
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * method which extracts all the claims from a JWT token
     * @param token
     * @return Map of claims
     */
    private Claims extractAllClaims(String token) {

        return Jwts
                .parser()
                .verifyWith(KEY)
                .build()
                .parseEncryptedClaims(token)
                .getPayload();

    }


    /**
     * method that check if the token is expired by extract the expiration date from JWT token and comparing it to now
     * @param token
     * @return true if the token is valid, otherwise it is expired
     */
    private Boolean isTokenExpired(String token) {

        return extractExpirationDate(token).before(new Date());

    }


    /**
     * method which calls the create token function with given UserDetails username and claims
     * @param userDetails
     * @return a valid JWT token
     */
    private String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        // subject will the username
        return createToken(claims, userDetails.getUsername());
    }


    private String createToken(Map<String, Object> claims, String subject) {

          return Jwts.builder()
                  .claims(claims)
                  .subject(subject)
                  .issuedAt(new Date())
                  .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                  .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                  .compact();
    }


    /**
     * method to check if the token is valid or not
     * @param token
     * @param userDetails
     * @return
     */
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }
}
