package account.service;

public class UserPayrollResponseDTO {
    private String name;
    private String lastname;
    private String period;
    private String salary;

    private UserPayrollResponseDTO(Builder builder) {
        this.name = builder.name;
        this.lastname = builder.lastname;
        this.period = builder.period;
        this.salary = builder.salary;
    }


    public String getName() {
        return name;
    }


    public String getLastname() {
        return lastname;
    }


    public String getPeriod() {
        return period;
    }


    public String getSalary() {
        return salary;
    }

    public static class Builder {
        private String name;
        private String lastname;
        private String period;
        private String salary;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder setPeriod(String period) {
            String month = period.substring(0,period.length() - 5);
            String year = period.substring(3);
            switch (month) {
                case "01":
                    month = "January";
                    break;
                case "02":
                    month = "February";
                    break;
                case "03":
                    month = "March";
                    break;
                case "04":
                    month = "April";
                    break;
                case "05":
                    month = "May";
                    break;
                case "06":
                    month = "June";
                    break;
                case "07":
                    month = "July";
                    break;
                case "08":
                    month = "August";
                    break;
                case "09":
                    month = "September";
                    break;
                case "10":
                    month = "October";
                    break;
                case "11":
                    month = "November";
                    break;
                case "12":
                    month = "December";
                    break;
                default:
                    month = "Invalid month";
                    break;
            }
            this.period = month + "-" + year;
            return this;
        }

        public Builder setSalary(Long salary) {
            if (salary >= 0) {
                Long cents = salary % 100;
                String dollars = "0";
                if(salary.toString().length() > 2){
                   dollars  = salary.toString().substring(0,salary.toString().length() -2);
                }

                this.salary = String.format("%s dollar(s) %d cent(s)",dollars,cents);
                return this;
            }
            throw new BadRequestException("Salary must be 0 or above!");


        }

        public UserPayrollResponseDTO build(){

            return new UserPayrollResponseDTO(this);
        }
    }


}
