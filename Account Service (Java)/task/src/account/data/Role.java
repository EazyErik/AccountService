package account.data;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    //removed getter and setter to save space
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;
    private String name;

    @ManyToMany(mappedBy = "userRoles")
    private Set<UserSignUp> users;

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, String code) {
        this.code = code;
        this.name = name;


    }

    public Role() {

    }

    public String getName() {
        return name;
    }
}