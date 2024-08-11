package account.service;

import account.config.PasswordEncoder;
import account.data.Role;
import account.data.UserSignUp;
import account.repository.RoleRepository;
import account.repository.UserSignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserSignUpService {
    @Autowired
    UserSignUpRepository userSignUpRepository;

    @Autowired
    RoleRepository roleRepository;

    ArrayList breachedPasswords = new ArrayList(List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"));

    @Autowired
    PasswordEncoder encoder;
//    public UserSignUpService(){
//        encoder = new BCryptPasswordEncoder(13);
//    }

    public UserSignUpDTO save(UserSignUp userSignUp){
        UserSignUp savedUser;
        try {
            if(userSignUpRepository.count() == 0){
                initAllRoles();
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByCode("ADM"));
               // roles.add(new Role("ADM","ROLE_ADMINISTRATOR",102L));
                userSignUp.setUserRoles(roles);

            }else{
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByCode("USR"));
                userSignUp.setUserRoles(roles);
            }
            userSignUp.setPassword(encoder.getEncoder().encode(userSignUp.getPassword()));
            savedUser = userSignUpRepository.save(userSignUp);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());

            throw new CustomException("User exist!");
        }


        return new UserSignUpDTO(
                savedUser.getName(), savedUser.getLastname(),
                savedUser.getEmail(),savedUser.getId());
    }

    public void changePassword(UserSignUp userSignUp){
        UserSignUp userFromDB = userSignUpRepository.findByEmailIgnoreCase(userSignUp.getEmail());
        userFromDB.setPassword(encoder.getEncoder().encode(userSignUp.getPassword()));
        userSignUpRepository.save(userFromDB);

    }

    public void initAllRoles(){
        roleRepository.saveAll(Arrays.asList(new Role("ROLE_ADMINISTRATOR","ADM"), new Role("ROLE_USER","USR"), new Role("ROLE_ACCOUNTANT","ACCT")));
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
