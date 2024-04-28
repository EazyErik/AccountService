package account.service;



public class SuccesfullPasswordChangeDTO{

    private String email;
    private String status;

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public SuccesfullPasswordChangeDTO(String email, String status) {
        this.email = email;
        this.status = status;
    }
}
