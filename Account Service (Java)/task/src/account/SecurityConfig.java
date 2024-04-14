package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                                .requestMatchers(antMatcher("/api/empl/payment")).authenticated()


//todo:AuthenticationProvider hier einbinden und custom Password in application properties akzeptieren
                        // other matchers
                )
                .sessionManagement(sessions -> sessions
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session
                );

        return http.build();
    }


//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        // http.csrf(AbstractHttpConfigurer::disable)
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
//                        authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
//                                .requestMatchers(antMatcher(HttpMethod.POST, "/api/auth/signup")).permitAll()
//                                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
//                                .requestMatchers(antMatcher("/api/empl/payment")).authenticated()
//                                )
//                .httpBasic(Customizer.withDefaults())
//                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        return http.build();
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        System.out.println("UserDetailsService");
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password("password")
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("admin")
                .password("password")
                .roles("USER", "ADMIN")
                .build());
        return manager;
    }

    @Autowired
    UserDetailsImpl userDetailService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder)
            throws Exception {
        System.out.println("AuthManager");
        // UserDetails userDetails = userDetailService.loadUserByUsername("ezzDoe@acme.com");
        //  System.out.println(userDetails);
        // UserDetails ezz = userDetailsImpl.loadUserByUsername("ezzDoe@acme.com");
        //  System.out.println(ezz);
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
