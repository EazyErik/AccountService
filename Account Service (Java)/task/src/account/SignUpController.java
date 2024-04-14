package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SignUpController {

    @Autowired
    UserSignUpService userSignUpService;

    @PostMapping("auth/signup")
    public ResponseEntity<Response> signup(@RequestBody UserSignUp userSignUp) {
        if (userSignUp.getName() != null && !userSignUp.getName().isEmpty() &&
                userSignUp.getLastname() != null &&
                userSignUp.getPassword() != null &&
                userSignUp.getEmail() != null &&
                !userSignUp.getLastname().isEmpty() &&
                userSignUp.getEmail().endsWith("@acme.com") &&
                !userSignUp.getPassword().isEmpty()
        ){

             userSignUp.setEmail(userSignUp.getEmail().toLowerCase());
            return ResponseEntity.ok(userSignUpService.save(userSignUp));
        }else{
            ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "/api/auth/signup");
            return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }

    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<CustomErrorMessage> handleCustomException(UserExistException ex, WebRequest request) {
        CustomErrorMessage body = new CustomErrorMessage(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false));
        System.out.println("Exception Handler");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }



    @GetMapping("/empl/payment")
    public ResponseEntity<Response> getPayment(){
        System.out.println("GETMapping");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
