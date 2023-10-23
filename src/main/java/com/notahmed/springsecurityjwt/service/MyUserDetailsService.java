package com.notahmed.springsecurityjwt.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

// service class that implements UserDetailsService from Spring Boot Security
@Service
public class MyUserDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        // will fetch from database the user

        // now the default user structure is from spring security

        return new User("notahmed","password", new ArrayList<>());
    }
}
