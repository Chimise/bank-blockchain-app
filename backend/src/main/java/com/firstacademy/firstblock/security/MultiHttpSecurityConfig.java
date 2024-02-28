package com.firstacademy.firstblock.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class MultiHttpSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager)
            throws Exception {
        http
                .cors(Customizer.withDefaults())
                .addFilter(new ApiJWTAuthenticationFilter(authenticationManager))
                .addFilter(new ApiJWTAuthorizationFilter(authenticationManager))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
                                "/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**",
                                "/images/**", "/scss/**", "/vendor/**", "/favicon.ico", "/auth/**", "/favicon.png",
                                "/v2/api-docs", "/configuration/ui", "/configuration/security",
                                "/webjars/**", "/swagger-resources/**", "/actuator", "/swagger-ui/**",
                                "/actuator/**", "/swagger-ui/index.html", "/swagger-ui/")
                        .permitAll()
                        // .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(Customizer.withDefaults())
                .sessionManagement(Customizer.withDefaults());

        return http.build();
    }

    // // @formatter:off
    //     protected void configure(HttpSecurity http) throws Exception {
    //         http
    //                 .cors()
    //                 .and()
    //                 .csrf()
    //                 .disable()
    //                 .antMatcher("/api/**")
    //                 .authorizeRequests()
    //                 .antMatchers("/api/v1/user/signup").permitAll()
    //                 .anyRequest()
    //                 .authenticated()
    //                 .and()
    //                 .exceptionHandling()
    //                 .authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
    //                 .and()
    //                 .addFilter(new ApiJWTAuthenticationFilter(authenticationManager()))
    //                 .addFilter(new ApiJWTAuthorizationFilter(authenticationManager()))
    //                 .sessionManagement()
    //                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    //     }
}
