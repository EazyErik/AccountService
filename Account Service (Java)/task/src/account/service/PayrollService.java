package account.service;

import account.data.Payroll;
import account.data.UserSignUp;
import account.repository.PayrollRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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


    public void validateEmails(List<Payroll> payments) {
        for (Payroll payroll : payments) {
            UserSignUp userSignUp = userSignUpService.loadUserByEmail(payroll.getEmployee());
            if (userSignUp == null) {
                throw new CustomException("The email does not exist: " + payroll.getEmployee());
            }
        }
    }

    public void validateSalaries(List<Payroll> payments) {
        for (Payroll payroll : payments) {
            validateSalary(payroll);

        }
    }

    public void validatePeriod(List<Payroll> payments) {
        for (Payroll payroll : payments) {
            Payroll payrollByEmail = loadPayrollByEmail(payroll.getEmployee());

            if (payrollByEmail != null && payroll.getEmployee().equals(payrollByEmail.getEmployee()) && payroll.getPeriod().equals(payrollByEmail.getPeriod())) {
                throw new CustomException("There exists already a payment for this employee in this period");
            }
        }
    }

    public Payroll loadPayrollByEmail(String email) {
        return payrollRepository.findFirstByEmployeeIgnoreCase(email);
    }


    public void update(Payroll payment) {


        validateSalary(payment);
        Payroll payroll = payrollRepository.findFirstByEmployeeIgnoreCaseAndPeriod(payment.getEmployee(),payment.getPeriod());
        if (payroll == null) {
            throw new CustomException("Cannot update, Payroll does not exist!");
        }
        payroll.setSalary(payment.getSalary());
        payrollRepository.save(payroll);


    }

    public void validateSalary(Payroll payment) {
        if (payment.getSalary() < 0) {
            throw new CustomException("Salary must be zero or above!");
        }
    }

    public List<UserPayrollResponseDTO> getAllPayments(UserSignUp currentUser) {
        List<Payroll> allByEmployeeIgnoreCase = payrollRepository.findAllByEmployeeIgnoreCase(currentUser.getEmail());
        Collections.sort(allByEmployeeIgnoreCase, (r1, r2) -> convertToDate(r2.getPeriod()).compareTo( convertToDate(r1.getPeriod())));
        System.out.println(allByEmployeeIgnoreCase);
        List<UserPayrollResponseDTO> response = new ArrayList<>();
        for (Payroll payroll : allByEmployeeIgnoreCase) {
            response.add(new UserPayrollResponseDTO.Builder()
                    .setName(currentUser.getName())
                    .setLastname(currentUser.getLastname())
                    .setPeriod(payroll.getPeriod())
                    .setSalary(payroll.getSalary())
                    .build());
        }



        // Print sorted users
        response.forEach(element -> System.out.println(element.getPeriod()));
        return response;
    }

    public Date convertToDate(String period) {
        System.out.println("convertToDate period: " +period);
        Date date = null;
        try {

            // Remove the angle brackets
            String cleanedDateString = period.substring(1, period.length() - 1);

            // Define the date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");

            // Parse the date
             date = dateFormat.parse(cleanedDateString);

            // Print the date
            System.out.println("Parsed date: " + date);
        } catch (ParseException e) {
            System.out.println("Period could not be parsed :" + period );

        }
  return date;

    }

    public UserPayrollResponseDTO getPayment(UserSignUp currentUser, String period) {
        System.out.println("current user: " + currentUser.getEmail() + ", period: " + period);
        //Payroll payroll = payrollRepository.findFirstByEmployeeIgnoreCaseAndPeriod(currentUser.getEmail(),"<"+period+">");
        //todo:analyse method findFirst... not working as expected, when more than 1 suitable record in db existed
       // if(payroll == null){
        Payroll payroll = null;
            for(Payroll payroll1: payrollRepository.findAllByEmployeeIgnoreCase(currentUser.getEmail())){
                System.out.println(payroll1.getPeriod() +":"+payroll1.getPeriod());
                if(payroll1.getPeriod().equals(period)) {
                    payroll = payroll1;
                }
                System.out.println(payroll1.getEmployee() +"," +payroll1.getPeriod());
            }


       // }
        if(payroll == null){
            throw new CustomException("Payroll does not exist!");
        }
       // Payroll payroll = payrollRepository.findFirstByEmployeeAndPeriodIgnoreCase(currentUser.getEmail(), period);
       // return new UserPayrollResponseDTO(currentUser.getName(), currentUser.getLastname(), payroll.getPeriod(), payroll.getSalary());
        return new UserPayrollResponseDTO.Builder()
                .setName(currentUser.getName())
                .setLastname(currentUser.getLastname())
                .setPeriod(period)
                .setSalary(payroll.getSalary())
                .build();
    }


}
