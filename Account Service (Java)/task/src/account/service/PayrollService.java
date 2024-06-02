package account.service;

import account.data.Payroll;
import account.data.UserSignUp;
import account.repository.PayrollRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayrollService {
    @Autowired
    PayrollRepository payrollRepository;

    @Autowired
    UserSignUpService userSignUpService;

    @Transactional
    public void addToPayments(List<Payroll> payments) {
        validateEmails(payments);
        validateSalaries(payments);
        validatePeriod(payments);
        try {
            payrollRepository.saveAll(payments);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CustomException("something went wrong!");
        }
    }


    public void validateEmails(List<Payroll>payments){
        for(Payroll payroll : payments){
            UserSignUp userSignUp = userSignUpService.loadUserByEmail(payroll.getEmployee());
            if(userSignUp == null){
                throw new CustomException("The email does not exist: " + payroll.getEmployee());
            }
        }
    }

    public void validateSalaries(List<Payroll>payments){
        for(Payroll payroll : payments){
         validateSalary(payroll);

        }
    }

    public void validatePeriod(List<Payroll>payments){
        for(Payroll payroll : payments){
            Payroll payrollByEmail = loadPayrollByEmail(payroll.getEmployee());

            if(payrollByEmail != null && payroll.getEmployee().equals(payrollByEmail.getEmployee()) && payroll.getPeriod().equals(payrollByEmail.getPeriod()) ){
                throw new CustomException("There exists already a payment for this employee in this period");
            }
        }
    }

    public Payroll loadPayrollByEmail(String email){
        return payrollRepository.findByEmployeeIgnoreCase(email);
    }


    public void update(Payroll payment) {
        validateSalary(payment);
        Payroll payroll = payrollRepository.findByEmployeeIgnoreCase(payment.getEmployee());
        if(payroll == null){
            throw new CustomException("Payroll does not exist!");
        }
        payroll.setSalary(payment.getSalary());
        payrollRepository.save(payroll);


    }

    public void validateSalary(Payroll payment){
        if(payment.getSalary() < 0){
            throw new CustomException("Salary must be zero or above!");
        }
    }
}
