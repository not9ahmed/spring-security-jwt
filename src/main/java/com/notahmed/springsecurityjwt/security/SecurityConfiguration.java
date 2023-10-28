package com.notahmed.springsecurityjwt.security;

import com.notahmed.springsecurityjwt.filters.JwtRequestFilter;
import com.notahmed.springsecurityjwt.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // field injection
//    @Autowired
//    MyUserDetailsService myUserDetailsService;

    private final MyUserDetailsService myUserDetailsService;


    private final JwtRequestFilter jwtRequestFilter;


    // this exists below
    // public DaoAuthenticationProvider authenticationProvider()
//    private final AuthenticationProvider authenticationProvider;



    // constructor injection
    public SecurityConfiguration(MyUserDetailsService myUserDetailsService, JwtRequestFilter jwtRequestFilter
//            , AuthenticationProvider authenticationProvider
    ) {
        this.myUserDetailsService = myUserDetailsService;

        this.jwtRequestFilter = jwtRequestFilter;
//        this.authenticationProvider = authenticationProvider;
    }



    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // will refer to the above dependency injection
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

        return authProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        // telling spring security to allow anyone to access the /authenticate endpoint
        // and for other request the user should be logged in
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/authenticate").permitAll()
                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(this.authenticationProvider())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);


//        Not required as i'm implementing everything from scratch
//        .formLogin(Customizer.withDefaults());

        // SessionCreationPolicy.STATELESS tells spring security there is no need for storing session


        // addFilterBefore makes sure to have the jwtRequestFilter working before the
        // spring security UsernamePasswordAuthenticationFilter

        return http.build();
    }



    // this is important to register the authenticationManager
    // this tell spring that there's bean called authentication manager
    // TODO read more about authenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }




}
