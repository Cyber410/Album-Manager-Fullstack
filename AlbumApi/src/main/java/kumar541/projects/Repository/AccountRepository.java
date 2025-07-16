package kumar541.projects.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kumar541.projects.Modal.Account;
import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    public abstract Optional<Account> findByEmail(String email);


}