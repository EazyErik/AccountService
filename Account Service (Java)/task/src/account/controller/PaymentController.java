package account.controller;

import account.data.Payroll;
import account.data.UserSignUp;
import account.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping
@RestController
public class PaymentController {

    @Autowired
    PayrollService payrollService;

    @Autowired
    UserSignUpService userSignUpService;

    @GetMapping("api/empl/payment")
    public ResponseEntity<Object> getPayments(){
        //todo:@PathVariable optional period
        //todo:finde alle Payments fuer die angemeldeten Benutzer, wenn keine Periode gegeben wurde, sonst finde payment fuer diese Periode
        //todo:period to date object und sortieren.
        //todo:neue ResponseDTO erstellen
        return null;
    }

    @PostMapping("api/acct/payments")
    public ResponseEntity<PaymentResponseDTO> addToPayments(@RequestBody List<Payroll> payments){

       payrollService.addToPayments(payments);
       return new ResponseEntity<>(new PaymentResponseDTO("Added successfully!"), HttpStatus.OK);
    }

    @PutMapping("api/acct/payments")
    public ResponseEntity<PaymentResponseDTO> updatePayment(@RequestBody Payroll payment){
       payrollService.update(payment);
        return  new ResponseEntity<>(new PaymentResponseDTO("Updated successfully!"),HttpStatus.OK);
    }


}
