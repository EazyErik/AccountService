//package account;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//public class UserSignUpDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserSignUpRepository userSignUpRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        UserSignUp user = userSignUpRepository.findByEmail(email);
//        if (user == null) {
//            throw new UsernameNotFoundException(email);
//        }
//        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
//        return new org.springframework.security.core.userdetails.User(
//                user.getName(),
//                user.getPassword(),
//                Collections.singleton(authority)
//        );
//    }
//}
