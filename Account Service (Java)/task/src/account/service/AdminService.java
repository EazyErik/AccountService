package account.service;

import account.controller.DeleteUserDTO;
import account.controller.UpdateRoleDTO;
import account.controller.UserDTO;
import account.data.Role;
import account.data.UserSignUp;
import account.repository.RoleRepository;
import account.repository.UserSignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    UserSignUpRepository userSignUpRepository;

    @Autowired
    RoleRepository roleRepository;

    public ArrayList<UserDTO> getUsers() {
        System.out.println("2");
        List<UserSignUp> all = userSignUpRepository.findAll();
        System.out.println("3");
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

    public UserDTO updateRole(UpdateRoleDTO updateRoleDTO){
        UserSignUp currentUser = userSignUpRepository.findByEmailIgnoreCase(updateRoleDTO.getUser());
        String dataBaseRole = "ROLE_" + updateRoleDTO.getRole();
       if(currentUser == null){
           throw new NotFoundException("User not found!");
        }

        switch (updateRoleDTO.getOperation()){
            case "GRANT":
                if(!currentUser.getUserRoles()
                        .stream().anyMatch(role -> role.getName().equalsIgnoreCase(dataBaseRole))){
                    System.out.println("2.");
                    Set<Role> userRoles = currentUser.getUserRoles();
                    userRoles.add(roleRepository.findByName(dataBaseRole));

                    currentUser.setUserRoles(userRoles);
                    userSignUpRepository.save(currentUser);
                }
                break;
            case "REMOVE":

                if(currentUser.getUserRoles()
                        .stream().anyMatch(role -> role.getName().equalsIgnoreCase(dataBaseRole))){

                    Set<Role> userRoles = currentUser.getUserRoles().stream()
                            .filter(role -> !role.getName().equalsIgnoreCase(dataBaseRole)).collect(Collectors.toSet());
                    currentUser.setUserRoles(userRoles);
                    if(userRoles.isEmpty()){
                        throw new BadRequestException("The user must have at least one role!");
                    }
                    userSignUpRepository.save(currentUser);
                }else{
                    throw new BadRequestException("The user does not have a role!");
                }
                break;
        }




        return new UserDTO(
                currentUser.getId(),
                currentUser.getName(),
                currentUser.getLastname(),
                currentUser.getEmail(),
                currentUser.getUserRoles().stream()
                        .map(role -> role.getName()).collect(Collectors.toSet()));
    }
}
