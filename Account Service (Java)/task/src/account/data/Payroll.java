package account.data;

public class Payroll {

    //todo: wir muessen uperberpruefen, dass der employee in der userSingUpTabelle ist, aber mit email vergleiche
    //employee with userEmail
    private String employee;
    private String period;
    private long salary;

    public Payroll(String employee, String period, long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
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
