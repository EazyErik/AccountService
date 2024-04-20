package account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSignUpRepository extends CrudRepository<UserSignUp,Long> {


    UserSignUp findByEmailIgnoreCase(String email);
}
