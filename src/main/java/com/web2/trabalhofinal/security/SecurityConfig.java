package com.web2.trabalhofinal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

     @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails vUser = User
            .withUsername("joaolavina")
            .password(passwordEncoder().encode("jgsl070105"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(vUser);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity pHttp) throws Exception {
        pHttp
            .csrf(pCsrf -> pCsrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(
                pSession -> pSession.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(
                pPath ->
                    pPath
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/funcionario/**").permitAll()
                        .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        
        return pHttp.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
  
}
