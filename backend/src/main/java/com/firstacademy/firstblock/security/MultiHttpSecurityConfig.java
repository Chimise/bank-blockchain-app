package com.firstacademy.firstblock.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstacademy.firstblock.dto.response.Response;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class MultiHttpSecurityConfig {

        @Autowired
        private Environment environment;

        @Bean
        public AuthenticationManager authenticationManager(
                        UserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {
                DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
                authenticationProvider.setUserDetailsService(userDetailsService);
                authenticationProvider.setPasswordEncoder(passwordEncoder);

                ProviderManager providerManager = new ProviderManager(authenticationProvider);
                return providerManager;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager)
                        throws Exception {
                http
                                .cors(cors -> cors.configurationSource(myWebsiteConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .formLogin(form -> form.disable())
                                .logout(ctx -> ctx.disable())
                                .exceptionHandling(exp -> exp.authenticationEntryPoint(this::handleAuthenticationError))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/error")
                                                .permitAll()
                                                .requestMatchers("/api/auth/login").permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(new ApiJWTAuthorizationFilter(),
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public WebSecurityCustomizer webCustomizer() {
   //@formatter:off
    return web -> {
      web.ignoring().requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**",
        "/images/**", "/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**", "/images/**", "/scss/**", "/vendor/**", "/favicon.ico", "/auth/**", "/favicon.png", "/v2/api-docs", "/configuration/ui","/configuration/security","/webjars/**", "/swagger-resources/**", "/actuator","/swagger-ui/**", "/actuator/**", "/swagger-ui/index.html", "/swagger-ui/", "/error", "/api/auth/login");
    };
    //@formatter:on
        }

        private CorsConfigurationSource myWebsiteConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList(environment.getProperty("cors.allowed-origins")));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                configuration.setAllowedHeaders(
                                Arrays.asList("Content-Type", "Authorization", "Origin", "Accept", "Cookie"));
                configuration.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        public void handleAuthenticationError(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException)
                        throws IOException, ServletException {

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                Response<Object> responseObj = Response.unauthorized();
                responseObj.addErrorMsgToResponse("An error occurred, could not log in", authException);

                final ObjectMapper mapper = new ObjectMapper();
                try {
                        mapper.writeValue(response.getOutputStream(), responseObj);
                } catch (Exception e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
        }

}