package account.controller;

import account.data.Payroll;
import account.data.UserSignUp;
import account.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping
@RestController
public class PaymentController {

    @Autowired
    PayrollService payrollService;

    @Autowired
    UserSignUpService userSignUpService;

    @GetMapping("api/empl/payment")
    public ResponseEntity<Object> getPayments(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false,name = "period") String period) {
        System.out.println("auth" +userDetails.getAuthorities());
        UserSignUp currentUser = userSignUpService.loadUserByEmail(userDetails.getUsername());
        System.out.println(currentUser.getUserRoles());
        if (period == null || period.isBlank()) {
            return new ResponseEntity(payrollService.getAllPayments(currentUser), HttpStatus.OK);
        }

      //todo:Wrong answer in test #48
        //
        //GET /api/empl/payment?period=01-2021 should respond with status code 200, responded: 400
        //Salary must be update!
        //Response body:
        //{
        //  "timestamp" : "2024-06-16T12:12:07.574278",
        //  "status" : 400,
        //  "error" : "Bad Request",
        //  "message" : "Payroll does not exist!",
        //  "path" : "/api/empl/payment"
        //}

        return  new ResponseEntity(payrollService.getPayment(currentUser,period),HttpStatus.OK);
    }

    @PostMapping("api/acct/payments")
    public ResponseEntity<PaymentResponseDTO> addToPayments(@RequestBody List<Payroll> payments) {
        for(Payroll payroll: payments){
            String month = payroll.getPeriod().substring(0,payroll.getPeriod().length() - 5);

            if(Integer.parseInt(month) > 12){
                throw new BadRequestException("Invalid date");
            }
        }

        payrollService.addToPayments(payments);
        return new ResponseEntity<>(new PaymentResponseDTO("Added successfully!"), HttpStatus.OK);
    }

    @PutMapping("api/acct/payments")
    public ResponseEntity<PaymentResponseDTO> updatePayment(@RequestBody Payroll payment) {
        payrollService.update(payment);
        return new ResponseEntity<>(new PaymentResponseDTO("Updated successfully!"), HttpStatus.OK);
    }


}
