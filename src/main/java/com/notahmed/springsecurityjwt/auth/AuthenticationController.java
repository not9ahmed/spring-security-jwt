package com.notahmed.springsecurityjwt.auth;

import com.notahmed.springsecurityjwt.model.AuthenticationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {



    @PostMapping("/")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {


        return null;
    }

}
