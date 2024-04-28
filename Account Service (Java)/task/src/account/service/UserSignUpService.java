package account.service;

import account.data.UserSignUp;
import account.repository.UserSignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSignUpService {
    @Autowired
    UserSignUpRepository userSignUpRepository;

    ArrayList breachedPasswords = new ArrayList(List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"));


    private final BCryptPasswordEncoder encoder;
    public UserSignUpService(){
        encoder = new BCryptPasswordEncoder(13);
    }

    public UserSignUpDTO save(UserSignUp userSignUp){
        UserSignUp savedUser;
        try {
            System.out.println("password: " + userSignUp.getPassword());
            System.out.println("Encoded password: " + encoder.encode(userSignUp.getPassword()));
            userSignUp.setPassword(encoder.encode(userSignUp.getPassword()));
            savedUser = userSignUpRepository.save(userSignUp);
        } catch (DataIntegrityViolationException e) {

            throw new CustomException("User exist!");
        }


        return new UserSignUpDTO(
                savedUser.getName(), savedUser.getLastname(),
                savedUser.getEmail(),savedUser.getId());
    }

    public UserSignUp loadUserByEmail(String email){
        UserSignUp user = userSignUpRepository.findByEmailIgnoreCase(email);
        return user;
    }

    public UserSignUpDTO transformFrom(UserSignUp user){
        UserSignUpDTO userSignUpDTO = new UserSignUpDTO(user.getName(), user.getLastname(),user.getEmail(), user.getId());
        return userSignUpDTO;
    }

    public boolean isBreached(String password){
        return breachedPasswords.contains(password);
    }

}
