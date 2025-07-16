package kumar541.projects.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import kumar541.projects.Modal.Account;
import kumar541.projects.Services.AccountService;
import kumar541.projects.utils.constatnts.Authority;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private AccountService accountService;


    @Override
    public void run(String... args) throws Exception {
    Account user1 = new Account();
    user1.setEmail("dev@gmail.com");
    user1.setPassword("password"); 
    user1.setAuthorities(Authority.USER.toString());

    Account user2 = new Account();
    user2.setEmail("john_doe1@random.com");
    user2.setPassword("secure456"); 
    user2.setAuthorities(Authority.ADMIN.toString() + " " + Authority.USER.toString());

    accountService.save(user1);
    accountService.save(user2);
}

}
