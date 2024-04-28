package account.service;

public class PasswordDTO {

    private String new_password;

    public PasswordDTO(String new_password) {
        this.new_password = new_password;
    }

    public String getNew_password() {
        return new_password;
    }
}
