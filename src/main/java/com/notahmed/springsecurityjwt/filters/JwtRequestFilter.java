package com.notahmed.springsecurityjwt.filters;

import com.notahmed.springsecurityjwt.service.MyUserDetailsService;
import com.notahmed.springsecurityjwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


// TODO read about the annotation @Component
@Component
// filter which intercept every request once
public class JwtRequestFilter extends OncePerRequestFilter {


    // check if the user exists

    private MyUserDetailsService myUserDetailsService;

    // validate the token

    private JwtUtil jwtUtil;

    public JwtRequestFilter(MyUserDetailsService myUserDetailsService, JwtUtil jwtUtil) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
    }


    // has chain to pass to other request or end request
    // examin incoming request and check if the token is correct
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;



        // check if the Authorization exists and if it starts with bearer
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//
//            System.out.println("inside if");
//            filterChain.doFilter(request, response);
//
//            // to stop execution of the filters
//            return;
//        }


        // check if the request has the header of authorization
        // and check if it has bearer word
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            // extract the jwt since it the auth header is not null
            // and it has the bearer word
            // and extract the username
            jwt = authorizationHeader.substring(7);

            System.out.println("jwt " + jwt);
            username = jwtUtil.extractUsername(jwt);

        }

        // check if the username is not null
        // and the user is not authenticated in SecurityContextHolder
        // then extract the userDetails object
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);

            // here check if the token is valid and the userdetails obj is matching
            // im also changing the normal behavior of spring boot
            // and simulating the regular flow which is seen with session id
            if (jwtUtil.isValidToken(jwt, userDetails)) {


                // this is the default spring security auth uses for managing authentication
                // in the context of username & password
                // TODO read more about these lines
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                // registering the authToken  to context holder
//                System.out.println("authenticationToken " + authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

        }

        filterChain.doFilter(request, response);

    }
}
