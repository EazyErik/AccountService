package account.repository;

import account.data.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByCode(String code);
    Role findByName(String name);

}
