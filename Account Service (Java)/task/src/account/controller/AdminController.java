package account.controller;

import account.data.Operation;
import account.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    UserSignUpService userSignUpService;


    @GetMapping("user/")

    public ResponseEntity<ArrayList<UserDTO>> getUsers() {
        System.out.println("1");
        return new ResponseEntity(adminService.getUsers(), HttpStatus.OK);
    }

    @DeleteMapping("user/{email}")
    public ResponseEntity<DeleteUserDTO> deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String email) {

        System.out.println(userDetails.getUsername());
        if (userDetails.getUsername().equalsIgnoreCase(email)) {
            throw new BadRequestException("Can't remove ADMINISTRATOR role!");
        }

        return new ResponseEntity<>(adminService.deleteUser(email), HttpStatus.OK);

    }

    @DeleteMapping("user/")
    public ResponseEntity<DeleteUserDTO> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        throw new AccessDeniedException("");
    }


    @PutMapping("user/role")
    public ResponseEntity<UserDTO> updateRole(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateRoleDTO updateRoleDTO) {

        List roles = new ArrayList(List.of("ADMINISTRATOR", "ACCOUNTANT", "USER", "AUDITOR"));
        List businessRoles = new ArrayList(List.of("ACCOUNTANT", "USER", "AUDITOR"));

        boolean isUpdatingAdmin = userDetails.getUsername().equalsIgnoreCase(updateRoleDTO.getUser());
        boolean isBusinessRole = businessRoles.contains(updateRoleDTO.getRole());
        if (updateRoleDTO.getRole().isBlank() || updateRoleDTO.getRole() == null || !roles.contains(updateRoleDTO.getRole())) {
            System.out.println("first if");
            throw new NotFoundException("Role not found!");

        } else if (!isUpdatingAdmin && updateRoleDTO.getRole().equalsIgnoreCase("ADMINISTRATOR")) {
            System.out.println("2nd if");
            throw new BadRequestException("The user cannot combine administrative and business roles!");

        } else if (isUpdatingAdmin) {
            System.out.println("3rd if");
            if (isBusinessRole && updateRoleDTO.getOperation().equalsIgnoreCase("GRANT")) {
                System.out.println("4th if");
                throw new BadRequestException("The user cannot combine administrative and business roles!");
            } else if (updateRoleDTO.getRole().equalsIgnoreCase("ADMINISTRATOR") && updateRoleDTO.getOperation().equalsIgnoreCase("REMOVE")) {
                System.out.println("5th if");
                throw new BadRequestException("Can't remove ADMINISTRATOR role!");
            }
        }


        return new ResponseEntity<>(adminService.updateRole(updateRoleDTO), HttpStatus.OK);

    }

    @PutMapping("user/access")
    public ResponseEntity<UserAccessResponseDTO> updateAccess(@AuthenticationPrincipal UserDetails userDetails,@RequestBody UserAccessDTO userAccessDTO) {

        boolean isUpdatingAdmin = userDetails.getUsername().equalsIgnoreCase(userAccessDTO.getUser());

        if(!(userAccessDTO.getOperation().equals(Operation.LOCK) ||
                userAccessDTO.getOperation().equals(Operation.UNLOCK))){
            throw new BadRequestException("");
        }
        if(isUpdatingAdmin && userAccessDTO.getOperation().equals(Operation.LOCK)){
            throw new BadRequestException("Can't lock the ADMINISTRATOR!");
        }
        userSignUpService.updateAccess(userAccessDTO);
        return null;

    }

}
