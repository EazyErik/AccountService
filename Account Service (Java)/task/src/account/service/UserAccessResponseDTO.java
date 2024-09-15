package account.service;

public class UserAccessResponseDTO {

    private String status;

    public String getStatus() {
        return status;
    }

    public UserAccessResponseDTO() {
    }

    public UserAccessResponseDTO(String status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
