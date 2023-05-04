package com.aptech.coursemanagementserver.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static com.aptech.coursemanagementserver.models.Role.ADMIN;
import static com.aptech.coursemanagementserver.models.Role.MANAGER;
import static com.aptech.coursemanagementserver.models.Permission.ADMIN_CREATE;
import static com.aptech.coursemanagementserver.models.Permission.ADMIN_DELETE;
import static com.aptech.coursemanagementserver.models.Permission.ADMIN_READ;
import static com.aptech.coursemanagementserver.models.Permission.ADMIN_UPDATE;
import static com.aptech.coursemanagementserver.models.Permission.MANAGER_CREATE;
import static com.aptech.coursemanagementserver.models.Permission.MANAGER_DELETE;
import static com.aptech.coursemanagementserver.models.Permission.MANAGER_READ;
import static com.aptech.coursemanagementserver.models.Permission.MANAGER_UPDATE;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    /*
     * SecurityFilterChain to use our JwtFilterChain
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html")// requestMatchers(...String) If the HandlerMappingIntrospector is available
                                           // in the classpath, maps to an MvcRequestMatcher that does not care which
                                           // HttpMethod is used.
                .permitAll() // Specify that URLs are allowed by anyone.

                // hasAnyRole Specifies that a user requires one of many roles
                // This mean the endpoint only accessible by user have the ADMIN or MANAGER role
                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())

                // hasAnyAuthority Specifies that a user requires one of many authorities
                // This mean the endpoint only accessible by user have the ADMIN_READ or
                // MANAGER_READ privilege
                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())

                .requestMatchers(POST, "/api/v1/management/**")
                .hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                .requestMatchers(PUT, "/api/v1/management/**")
                .hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                .requestMatchers(DELETE, "/api/v1/management/**")
                .hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())

                /*
                 * .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
                 * .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
                 * .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
                 * .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
                 * .requestMatchers(DELETE,
                 * "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())
                 */

                .anyRequest() // Maps any request.
                .authenticated() // Attempts to authenticate the passed Authentication object, returning a fully
                                 // populated Authentication object (including granted authorities) if
                                 // successful.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Spring will create new Session for each
                                                                        // Request
                .and()
                .authenticationProvider(authenticationProvider)// authenticationProvider use DAOAuthenticationProvider
                                                               // from AppConfiguration
                                                               // authenticationProvider() Allows adding an additional
                                                               // AuthenticationProvider to be used

                // Add our JwtAuthFilter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }
}
