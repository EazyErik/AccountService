package account.service;



public class UserSignUpDTO{

    private Long id;
    private String name;
    private String lastname;
    private String email;

    public UserSignUpDTO(String name, String lastname, String email,Long id) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.id = id;

    }

    public UserSignUpDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
