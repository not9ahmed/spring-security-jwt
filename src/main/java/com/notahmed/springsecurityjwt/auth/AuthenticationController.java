package com.notahmed.springsecurityjwt.auth;

import com.notahmed.springsecurityjwt.model.AuthenticationRequest;
import com.notahmed.springsecurityjwt.model.AuthenticationResponse;
import com.notahmed.springsecurityjwt.service.MyUserDetailsService;
import com.notahmed.springsecurityjwt.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    // this got changed
//    @Autowired
//    private final AuthenticationManager authenticationManager;

    private final AuthenticationService authenticationService;

    private final MyUserDetailsService userDetailsService;


    private final JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationService authenticationService, MyUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }


    // here i am implementing the manager
    // TODO move to a service class
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//
//        return config.getAuthenticationManager();
//
//    }


    @PostMapping("/")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {


        // here I am calling spring auth method
        // and i am specifying the auth type which is username and password
        // this method throws an exception in case the auth fails

//        try {
////            authenticationService.authenticateUser(
////
////                    new UsernamePasswordAuthenticationToken(
////                            authenticationRequest.username(),
////                            authenticationRequest.password()
////                    )
////            );
//
//            authenticationService.authenticateUser(
//
//                    authenticationRequest
//            );
//
//        } catch (BadCredentialsException e) {
//            throw new BadCredentialsException("Incorrect username or password ", e);
//        }
//
//
//        // TODO why use final??
//        // loads a userdetails object from the username
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());
//
//
//        // generate a token from the user details object
//        final String token = jwtUtil.generateToken(userDetails);

        final AuthenticationResponse authenticationResponse   = authenticationService.authenticateUser(authenticationRequest);


        // returns a response entity object
        return ResponseEntity.ok(authenticationResponse);

    }


}
