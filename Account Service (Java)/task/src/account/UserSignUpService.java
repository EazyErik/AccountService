package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserSignUpService {
    @Autowired
    UserSignUpRepository userSignUpRepository;

    public UserSignUpDTO save(UserSignUp userSignUp){
        UserSignUp savedUser;
        try {
           savedUser = userSignUpRepository.save(userSignUp);
        } catch (DataIntegrityViolationException e) {

            throw new UserExistException("User exist!");
        }


        return new UserSignUpDTO(
                savedUser.getName(), savedUser.getLastname(),
                savedUser.getEmail(),savedUser.getId());
    }

    public UserSignUp loadUserByEmail(String email){
        UserSignUp user = userSignUpRepository.findByEmail(email);
        return user;
    }

}
