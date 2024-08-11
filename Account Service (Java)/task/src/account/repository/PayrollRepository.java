package account.repository;

import account.data.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PayrollRepository extends JpaRepository<Payroll,String> {

     Payroll findFirstByEmployeeIgnoreCase(String email);
    Payroll findFirstByEmployeeIgnoreCaseAndPeriod(String email, String period);
   // Payroll findByEmployeeAndByPeriodIgnoreCase(String email,String period);

    List<Payroll> findAllByEmployeeIgnoreCase(String email);
}
