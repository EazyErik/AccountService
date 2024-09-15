package account.service;

public class UserAccessDTO {

    private String user;
    private Enum operation;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Enum getOperation() {
        return operation;
    }

    public void setOperation(Enum operation) {
        this.operation = operation;
    }
}
