package com.notahmed.springsecurityjwt.model;

public record AuthenticationRequest(
        String username,
        String password
) { }
