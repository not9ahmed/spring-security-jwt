package com.notahmed.springsecurityjwt.auth;

import com.notahmed.springsecurityjwt.model.AuthenticationRequest;
import com.notahmed.springsecurityjwt.model.AuthenticationResponse;
import com.notahmed.springsecurityjwt.service.MyUserDetailsService;
import com.notahmed.springsecurityjwt.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final AuthenticationProvider authenticationProvider;

    private final MyUserDetailsService userDetailsService;


    private final JwtUtil jwtUtil;



    public AuthenticationService(AuthenticationManager authenticationManager, AuthenticationProvider authenticationProvider, MyUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authenticationProvider = authenticationProvider;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();

    }

    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.username(),
                            authenticationRequest.password()
                    )
            );

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password ", e);
        }

        // TODO why use final??
        // loads a userdetails object from the username
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());


        // generate a token from the user details object
        final String token = jwtUtil.generateToken(userDetails);


        // returns a response entity object
        return ResponseEntity.ok(new AuthenticationResponse(token));

//        return null;
    }



}
