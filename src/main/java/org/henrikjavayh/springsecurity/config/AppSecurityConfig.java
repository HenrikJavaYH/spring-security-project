package org.henrikjavayh.springsecurity.config;


import org.henrikjavayh.springsecurity.user.autthority.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
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

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public AppSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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
                .formLogin(Customizer.withDefaults());

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
