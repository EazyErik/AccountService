package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {
    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("hier");

        http
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(restAuthenticationEntryPoint)) // Handle auth errors
                .csrf(csrf -> csrf.disable()) // For Postman
                .headers(headers -> headers.frameOptions().disable()) // For the H2 console
                .authorizeHttpRequests(auth -> auth  // manage access
                                .requestMatchers(antMatcher(HttpMethod.POST, "/api/auth/signup")).permitAll()
                                .requestMatchers(antMatcher(HttpMethod.POST, "/actuator/shutdown")).permitAll()
                                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                                .requestMatchers(antMatcher("/api/empl/payment")).permitAll().anyRequest().authenticated()



                        // other matchers
                )
                .sessionManagement(sessions -> sessions
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session
                );

        return http.build();
    }


//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//        System.out.println("UserDetailsService");
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("user")
//                .password(encoder.encode("password"))
//                .roles("USER")
//                .build());
//        manager.createUser(User.withUsername("admin")
//                .password(encoder.encode("password"))
//                .roles("USER", "ADMIN")
//                .build());
//        return manager;
//    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
