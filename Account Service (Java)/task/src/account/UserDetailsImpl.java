package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    UserSignUpRepository userSignUpRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    public UserDetailsImpl(){
        System.out.println("Constructor UserDetailsImpl");
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByEmail");
        UserSignUp user = userSignUpRepository.findByEmailIgnoreCase(email);
        System.out.println("user:" + user);
        if(user == null){
            throw new UsernameNotFoundException("No user with this email: " + email);
        }
        System.out.println("password: " + user.getPassword());
        System.out.println("Encoded password: " + encoder.encode(user.getPassword()));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
