package account.config;

import account.data.UserSignUp;
import account.repository.UserSignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDetailsImpl implements UserDetailsService {

    @Autowired(required = false)
    UserSignUpRepository userSignUpRepository;


    public UserDetailsImpl() {
        System.out.println("Constructor UserDetailsImpl");

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserSignUp user = userSignUpRepository.findByEmailIgnoreCase(email);

        if (user == null) {

            throw new UsernameNotFoundException("No user with this email: " + email);
        }

        user.getUserRoles().stream().map(role -> {
            System.out.println(role);
            return role;
        });
        Set<GrantedAuthority> authorities = user.getUserRoles().stream()
                .map(role -> {
                    System.out.println("Role:" + role.getName());
                    return new SimpleGrantedAuthority(role.getName());
                })
                .collect(Collectors.toSet());
        System.out.println("test"+authorities);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                //.roles("USER")
                .build();
    }
}
