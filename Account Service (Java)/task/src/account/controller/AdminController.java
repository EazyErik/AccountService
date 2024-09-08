package account.controller;

import account.service.AdminService;
import account.service.BadRequestException;
import account.service.NotFoundException;
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
        //todo: anpassen der error messages nicht bezogen auf admin darf man keine Rolle entziehen, je nachdem bekommt man eine unterschiedliche Error message
        List roles = new ArrayList(List.of("ADMINISTRATOR", "ACCOUNTANT", "USER"));
        List businessRoles = new ArrayList(List.of("ACCOUNTANT", "USER"));
        System.out.println("Admin: " + userDetails.getUsername());
        System.out.println("updated user: " + updateRoleDTO.getUser() );
        System.out.println("role: " + updateRoleDTO.getRole());
        System.out.println("operation: " +updateRoleDTO.getOperation());
        boolean isUpdatingAdmin = userDetails.getUsername().equalsIgnoreCase(updateRoleDTO.getUser());
        if (updateRoleDTO.getRole().isBlank() || updateRoleDTO.getRole() == null || !roles.contains(updateRoleDTO.getRole())) {
            System.out.println("first if");
            throw new NotFoundException("Role not found!");

        } else if (!isUpdatingAdmin && updateRoleDTO.getRole().equalsIgnoreCase("ADMINISTRATOR")) {
            System.out.println("2nd if");
            throw new BadRequestException("The user cannot combine administrative and business roles!");

        } else if (isUpdatingAdmin) {
            System.out.println("3rd if");
            if(businessRoles.contains(updateRoleDTO.getRole()) && updateRoleDTO.getOperation().equalsIgnoreCase("GRANT")){
                System.out.println("4th if");
                throw new BadRequestException("The user cannot combine administrative and business roles!");
            }
           else if(updateRoleDTO.getRole().equalsIgnoreCase("ADMINISTRATOR") && updateRoleDTO.getOperation().equalsIgnoreCase("REMOVE")){
                System.out.println("5th if");
                throw new BadRequestException("Can't remove ADMINISTRATOR role!");
            }
        }



        return new ResponseEntity<>(adminService.updateRole(updateRoleDTO), HttpStatus.OK);

    }


}
