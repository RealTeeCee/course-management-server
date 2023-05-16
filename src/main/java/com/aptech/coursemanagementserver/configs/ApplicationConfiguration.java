package com.aptech.coursemanagementserver.configs;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;
import static org.modelmapper.convention.NamingConventions.JAVABEANS_MUTATOR;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aptech.coursemanagementserver.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final UserRepository userRepository;

    @Bean
    // https://modelmapper.org/user-manual/configuration/
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        // modelMapper.getConfiguration().setFieldMatchingEnabled(true)
        // .setFieldAccessLevel(PRIVATE)
        // .setSourceNamingConvention(JAVABEANS_MUTATOR); // JAVABEAN Convention:
        // // https://docstore.mik.ua/orelly/java-ent/jnut/ch06_02.htm
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // get Email from our User (the lambda will return a UserDetails -> User
        // implement UserDetails)
        // @Override
        // public UserDetails loadUserByUsername(String email) throws
        // UsernameNotFoundException {
        // return userRepository.findByEmail(email)
        // .orElseThrow(() -> new UsernameNotFoundException("Invalid username or
        // password"));
        // }
        return (email) -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
    }

    @Bean
    // AuthenticationProvider is a Data Access Object (DAO) which is responsible to
    // fetch userDetails, encode password, ...
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService()); // Tell this DAOProvider which user details
                                                                            // which userDetailsService to use to fetch
                                                                            // information of user
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    // AuthenticationManager use to process an Authentication request.
    // AuthenticationConfiguration exports the authentication Configuration
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
