package com.example.ap_ment.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        //without jwt
        http.securityContext((context) -> context.requireExplicitSave(false))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        http
                .authorizeHttpRequests(request -> request.requestMatchers(
                                "/apment/v1/auth/signup", "/apment/v1/auth/signin"
                                ,"/apment/v1/auth/loginbygoogle"
                        ).permitAll()
                        .requestMatchers("/apment/v1/user").hasRole("MANAGER")    //for roles
                        .anyRequest().authenticated());




        http.logout(logout -> logout.logoutUrl("/logout").permitAll()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(((request, response, authentication) ->
                        SecurityContextHolder.clearContext())));
        http
                .authenticationProvider(authenticationProvider);

        return http.build();
    }
}