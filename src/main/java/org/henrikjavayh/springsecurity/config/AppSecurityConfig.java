package org.henrikjavayh.springsecurity.config;


import org.henrikjavayh.springsecurity.user.autthority.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {


    private final UserDetailsService userDetailsService;
    private final String rememberMeKey;

    @Autowired
    public AppSecurityConfig(UserDetailsService userDetailsService, @Value("{remember.me.key}") String rememberMeKey) {
        this.userDetailsService = userDetailsService; //CustomUserDetailsService
        this.rememberMeKey = rememberMeKey;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(
                        auth -> auth
                                //True by default?
                                .requestMatchers("/", "/register", "/static/**").permitAll()
                                .requestMatchers("/debug/**").permitAll()
                                .requestMatchers("/admin", "/tools").hasRole("ADMIN")
                                .requestMatchers("/user").hasRole(UserRole.USER.name())
                                .anyRequest().authenticated()


                )
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login").permitAll()
                        .loginProcessingUrl("/authenticate")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .failureUrl("/login?error")
                        .defaultSuccessUrl("/")//.false - default


                )
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/logout").permitAll()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .logoutSuccessUrl("/login?logout")
                )

                .rememberMe(rememberMeConfigurer -> rememberMeConfigurer
                        .key(rememberMeKey)
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(24))
                        .userDetailsService(userDetailsService)


                );


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
