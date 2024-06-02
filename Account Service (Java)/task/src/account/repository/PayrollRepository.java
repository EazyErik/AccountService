package account.repository;

import account.data.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PayrollRepository extends JpaRepository<Payroll,String> {

     Payroll findByEmployeeIgnoreCase(String email);
}
