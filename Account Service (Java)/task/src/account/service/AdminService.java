package account.service;

import account.controller.DeleteUserDTO;
import account.controller.UserDTO;
import account.data.UserSignUp;
import account.repository.UserSignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    UserSignUpRepository userSignUpRepository;

    public ArrayList<UserDTO> getUsers() {
        List<UserSignUp> all = userSignUpRepository.findAll();
        Collections.sort(all, Comparator.comparingLong(UserSignUp::getId));
        return all.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getLastname(),
                        user.getEmail(),
                        user.getUserRoles().stream()
                                .map(role -> role.getName()).collect(Collectors.toSet())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public DeleteUserDTO deleteUser(String email){
        UserSignUp byEmailIgnoreCase = userSignUpRepository.findByEmailIgnoreCase(email);
                if(byEmailIgnoreCase == null){
                    throw new NotFoundException("User not found!");
                }else{
                    userSignUpRepository.delete(byEmailIgnoreCase);
                    return new DeleteUserDTO(byEmailIgnoreCase.getEmail(),"Deleted successfully!");
                }
    }
}
