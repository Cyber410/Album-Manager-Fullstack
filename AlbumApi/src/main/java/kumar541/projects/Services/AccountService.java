package kumar541.projects.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kumar541.projects.Modal.Account;
import kumar541.projects.Repository.AccountRepository;
import kumar541.projects.utils.constatnts.Authority;


@Service
public class AccountService implements UserDetailsService {
    
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account){
        
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if(account.getAuthorities()==null){
            account.setAuthorities(Authority.USER.toString());
        }
        return accountRepository.save(account);

    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }

     public void deleteById(Long id){
       accountRepository.deleteById(id);
    }

    public Optional<Account> findByEmail(String email){
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findById(Long id){
        return accountRepository.findById(id);
    }
    
   @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Account> account = accountRepository.findByEmail(email);

        if (!account.isPresent()) {
            throw new UsernameNotFoundException("User not found!");
        }

        Account acc = account.get();

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(acc.getAuthorities()));

        return new User(acc.getEmail(), acc.getPassword(), grantedAuthorities);
    }


}
