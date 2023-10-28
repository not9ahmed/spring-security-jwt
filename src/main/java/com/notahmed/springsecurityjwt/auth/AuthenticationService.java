package com.notahmed.springsecurityjwt.auth;

import com.notahmed.springsecurityjwt.model.AuthenticationRequest;
import com.notahmed.springsecurityjwt.model.AuthenticationResponse;
import com.notahmed.springsecurityjwt.service.MyUserDetailsService;
import com.notahmed.springsecurityjwt.util.JwtUtil;


import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;


    private final MyUserDetailsService myUserDetailsService;


    private final JwtUtil jwtUtil;



    public AuthenticationService(AuthenticationManager authenticationManager, MyUserDetailsService myUserDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
    }



    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password ", e);
        }

        // TODO why use final??
        // loads a userdetails object from the username
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(request.username());


        // generate a token from the user details object
        final String token = jwtUtil.generateToken(userDetails);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);

        // returns a response entity object
        return authenticationResponse;

//        return null;
    }





}
