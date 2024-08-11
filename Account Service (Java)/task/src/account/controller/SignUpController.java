package account.controller;

//import account.service.ErrorResponse;

import account.config.PasswordEncoder;
import account.data.Payroll;
import account.data.UserSignUp;
import account.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api")
public class SignUpController {

    @Autowired
    UserSignUpService userSignUpService;

    @Autowired
    BCryptPasswordEncoder encoder;

    @PostMapping("auth/signup")
    public ResponseEntity<UserSignUpDTO> signup(@RequestBody UserSignUp userSignUp) {
        checkValidUser(userSignUp);
        validatePasswordSecurity(userSignUp.getPassword());
        userSignUp.setEmail(userSignUp.getEmail().toLowerCase());
        return ResponseEntity.ok(userSignUpService.save(userSignUp));


    }


    private void checkValidUser(UserSignUp userSignUp) {
        if (
                userSignUp.getName() != null && !userSignUp.getName().isEmpty() &&
                        userSignUp.getLastname() != null &&
                        userSignUp.getPassword() != null &&
                        userSignUp.getEmail() != null &&
                        !userSignUp.getLastname().isEmpty() &&
                        userSignUp.getEmail().endsWith("@acme.com") &&
                        !userSignUp.getPassword().isEmpty()) {

        } else {
            throw new CustomException("");
        }

    }

    private void validatePasswordSecurity(String password) {
        if (password.length() < 12) {
            throw new CustomException("Password length must be 12 chars minimum!");

        } else {
            if (userSignUpService.isBreached(password)) {
                throw new CustomException("The password is in the hacker's database!");
            }
        }
    }

    @GetMapping("auth/test")
    public ResponseEntity<PasswordDTO> testValidate() {
        return ResponseEntity.ok(new PasswordDTO("apfelkuchenistlecker"));
    }

    @PostMapping("auth/changepass")
    public ResponseEntity<SuccesfullPasswordChangeDTO> changePassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PasswordDTO passwordDTO) {
        UserSignUp currentUser = userSignUpService.loadUserByEmail(userDetails.getUsername());

        if (encoder.matches(passwordDTO.getNew_password(), currentUser.getPassword())) {
            throw new CustomException("The passwords must be different!");
        }
        currentUser.setPassword(passwordDTO.getNew_password());
        validatePasswordSecurity(currentUser.getPassword());
        userSignUpService.changePassword(currentUser);
        return ResponseEntity.ok(new SuccesfullPasswordChangeDTO(currentUser.getEmail(), "The password has been updated successfully"));

    }


//    @GetMapping("/empl/payment")
//    public ResponseEntity<UserSignUpDTO> getPayment(@AuthenticationPrincipal UserDetails userDetails) {
//        System.out.println("GETMapping");
//        UserSignUp currentUser = userSignUpService.loadUserByEmail(userDetails.getUsername());
//        UserSignUpDTO userSignUpDTO = userSignUpService.transformFrom(currentUser);
//
//        System.out.println(userDetails);
//        return new ResponseEntity<>(userSignUpDTO, HttpStatus.OK);
//    }
}
