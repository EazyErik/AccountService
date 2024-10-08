package account.service;

import account.config.PasswordEncoder;
import account.controller.UserDTO;
import account.data.Operation;
import account.data.Role;
import account.data.UserSignUp;
import account.repository.RoleRepository;
import account.repository.UserSignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public UserDTO save(UserSignUp userSignUp){
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

            throw new BadRequestException("User exist!");
        }


        return new UserDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getLastname(),
                savedUser.getEmail(),
                savedUser.getUserRoles().stream()
                        .map(role -> role.getName()).collect(Collectors.toSet()));
    }
    //todo:implement that inactive user is blocked!
    public UserAccessResponseDTO updateAccess(UserAccessDTO userAccessDTO){
        UserSignUp currentUser = userSignUpRepository.findByEmailIgnoreCase(userAccessDTO.getUser());
        if(currentUser != null){
            if(userAccessDTO.getOperation().equals(Operation.LOCK)){
                currentUser.setActive(false);
                userSignUpRepository.save(currentUser);
                return new UserAccessResponseDTO("User " + currentUser.getEmail() + " locked!");
            }else if(userAccessDTO.getOperation().equals(Operation.UNLOCK)){
                currentUser.setActive(true);
                return new UserAccessResponseDTO("User " + currentUser.getEmail() + " unlocked!");
            }
        }
        throw new NotFoundException("Can't find user!");



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
