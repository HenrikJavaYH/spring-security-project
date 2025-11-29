package org.henrikjavayh.springsecurity.config;


import org.henrikjavayh.springsecurity.security.jwt.JwtAuthenticationFilter;
import org.henrikjavayh.springsecurity.user.autthority.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Autowired
    public AppSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity
                .csrf(csrfConfigurer -> csrfConfigurer.disable())
                .authorizeHttpRequests(auth -> auth
                                //True by default
                                .requestMatchers("/", "/register", "/static/**", "/login").permitAll()
                                .requestMatchers("/debug/**").permitAll()
                                .requestMatchers("/admin", "/tools").hasRole("ADMIN")
                                .requestMatchers("/user").hasRole(UserRole.USER.name())
                                .anyRequest().authenticated()

        )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }

    /*@Bean
    public UserDetailsService debugUserCreation() {

        UserDetails Benny = User.builder()
                .username("Benny")
                .password(passwordEncoder.encode("123"))
                .authorities(UserRole.USER.getUserAuthorities())
                //.passwordEncoder(passwordEncoder::encode)
                //.roles("USER")
                .build();

        UserDetails Frida = User.builder()
                .username("Frida")
                .password(passwordEncoder.encode("321"))
                .authorities(UserRole.ADMIN.getUserAuthorities())
                //.passwordEncoder(passwordEncoder::encode)
                //.roles("USER")
                .build();


        return new InMemoryUserDetailsManager(Benny, Frida);
    } */
}
