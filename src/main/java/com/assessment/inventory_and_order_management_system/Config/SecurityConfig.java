package com.assessment.inventory_and_order_management_system.Config;


import com.assessment.inventory_and_order_management_system.Security.JwtAuthenticationFilter;
import com.assessment.inventory_and_order_management_system.Security.JwtService;
import com.assessment.inventory_and_order_management_system.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwt;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;



    @Bean
    public AuthenticationProvider authenticationProvider() {
         DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
         daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
         return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers(
//                                "/api/v1/items/add",
//                                "/api/v1/items/update",
//                                "/api/v1/items/delete/{id}",
//                                "/api/v1/order-item/total-orders-for-item",
//                                "/api/v1/order-item/item/{itemId}",
//                                "/api/v1/order/all",
//                                "/api/v1/order/delete/{id}",
//                                "/api/v1/order-item/all"
//                        ).authenticated()
                        .requestMatchers(
                                "/api/v1/user/**",
                                "/api/v1/items/all",
                                "/api/v1/items/find/{id}",
                                "/api/v1/order/save",
                                "api/v1/order/find/{id}",
                                "/api/v1/order-item/user/{itemId}/{username}",
                                "/api/v1/order-item/total-orders-for-item-for-user"

                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
