package account.controller;

import account.service.AdminService;
import account.service.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService ;

    @GetMapping("user")
    public ResponseEntity<ArrayList<UserDTO>> getUsers() {
        return new ResponseEntity(adminService.getUsers(),HttpStatus.OK);
    }
    @DeleteMapping("user/{email}")
    public ResponseEntity<DeleteUserDTO> deleteUser(@AuthenticationPrincipal UserDetails userDetails,@PathVariable String email){
        System.out.println(userDetails.getUsername());
        if(userDetails.getUsername().equalsIgnoreCase(email)){
            throw new BadRequestException("Can't remove ADMINISTRATOR role!");
        }
        return new ResponseEntity<>(adminService.deleteUser(email),HttpStatus.OK);

    }
}
