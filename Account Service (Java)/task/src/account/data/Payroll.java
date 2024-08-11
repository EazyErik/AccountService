package account.data;

import jakarta.persistence.*;

@Entity
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String employee;
    private String period;
    private long salary;
//    @OneToMany
//    @JoinColumn(referencedColumnName = "email")
//    private UserSignUp userSignUp;



    public Payroll(String employee, String period, long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Payroll{" +
                "employee='" + employee + '\'' +
                ", period='" + period + '\'' +
                ", salary=" + salary +
           //     ", userSignUp=" + userSignUp +
                '}';
    }

    public Payroll() {
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}
