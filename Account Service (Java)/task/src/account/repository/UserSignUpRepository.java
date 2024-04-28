package account.repository;

import account.data.UserSignUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserSignUpRepository extends JpaRepository<UserSignUp,Long> {


    UserSignUp findByEmailIgnoreCase(String email);
}
