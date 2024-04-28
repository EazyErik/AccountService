package account.config;

import account.data.UserSignUp;
import account.service.UserSignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserSignUpService userSignUpService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserSignUp userSignUp = userSignUpService.loadUserByEmail(email);
       // UserDetails userDetails = userSignUpService.loadUserByEmail(email);
         if(userSignUp == null){
             throw new BadCredentialsException("Email not exist!");
         }
        if (!password.equals(userSignUp.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        return new UsernamePasswordAuthenticationToken(email, password, null);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
