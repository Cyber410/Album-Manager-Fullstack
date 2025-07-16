package kumar541.projects.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kumar541.projects.Modal.Account;
import kumar541.projects.Services.AccountService;
import kumar541.projects.Services.TokenService;
import kumar541.projects.payload.auth.AccountDTO;
import kumar541.projects.payload.auth.AccountViewDTO;
import kumar541.projects.payload.auth.AuthoritiesDTO;
import kumar541.projects.payload.auth.PasswordDTO;
import kumar541.projects.payload.auth.ProfileDTO;
import kumar541.projects.payload.auth.TokenDTO;
import kumar541.projects.payload.auth.UserLoginDTO;
import kumar541.projects.utils.constatnts.AccountError;
import kumar541.projects.utils.constatnts.AccountSuccess;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name="Auth Controller",description="Controller for Account managment")
@Slf4j
@CrossOrigin(origins="http://localhost:3000",maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenDTO> token(@Valid @RequestBody UserLoginDTO userLogin) throws AuthenticationException{
        
        try{
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword())
             );
            return ResponseEntity.ok(new TokenDTO(tokenService.generateToken(authentication)));
            
        }

        catch(Exception ex){
            log.debug(AccountError.TOKEN_GENERATION_ERROR + " " + ex.getMessage());
            return new ResponseEntity<>(new TokenDTO(null),HttpStatus.BAD_REQUEST);
        }
    }

   
    
     @PostMapping(value="/users/add",consumes = "application/json" )
     @ResponseStatus(HttpStatus.CREATED)
     @Operation(summary="Add a new user")
     @ApiResponse(responseCode="400",description = "Enter a valid email or password")
     @ApiResponse(responseCode="200",description = "Account Created")
    public ResponseEntity<String> addUser(@Valid @RequestBody AccountDTO accountDTO){

        try {
            Account account= new Account();
            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());
            accountService.save(account);
            return ResponseEntity.ok(AccountSuccess.ACCOUNT_ADDED.toString() );
        } catch (Exception e) {
            log.debug(AccountError.ADD_ACCOUNT_ERROR +" "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }


    @PostMapping(value="/users",produces ="application/json")
    @ApiResponse(responseCode="200",description = "List of users")
    @ApiResponse(responseCode="401",description = "Token Missing")
    @ApiResponse(responseCode="403",description = "Token Error")
    @Operation(summary="List users")
    @SecurityRequirement(name="Album Api")
    public List<AccountViewDTO> Users(){

        List<AccountViewDTO> list= new ArrayList<>();
        for(Account acc: accountService.findAll()){
            list.add(new AccountViewDTO(acc.getId(), acc.getEmail(), acc.getAuthorities()));
        }
            
        return list;

    }

    @PostMapping(value="/profile",produces ="application/json")
    @ApiResponse(responseCode="200",description = "Profile")
    @ApiResponse(responseCode="401",description = "Token Missing")
    @ApiResponse(responseCode="403",description = "Token Error")
    @Operation(summary=" Get User Profile")
    @SecurityRequirement(name="Album Api")
    public ProfileDTO profile(Authentication authentication){

        String email= authentication.getName();  // it return the username or email (or anything) ie used as usename

        Optional<Account> acc= accountService.findByEmail(email);

            Account acc1= acc.get();
            ProfileDTO profileDTO= new ProfileDTO(acc1.getId(),acc1.getEmail(), acc1.getAuthorities());
            return profileDTO;
    
    }

    @PutMapping(value="/profile/update-password",produces ="application/json",consumes ="application/json")
    @ApiResponse(responseCode="200",description = "Password Updated")
    @ApiResponse(responseCode="401",description = "Token Missing")
    @ApiResponse(responseCode="403",description = "Token Error")
    @Operation(summary="Update Profile Password")
    @SecurityRequirement(name="Album Api")
    public AccountViewDTO updatePassword( @Valid @RequestBody PasswordDTO passwordDTO,Authentication authentication){

        String email= authentication.getName();  // it return the username or email (or anything) ie used as usename

        Optional<Account> acc= accountService.findByEmail(email);
            Account acc1= acc.get();
            acc1.setPassword(passwordDTO.getPassword());
            accountService.save(acc1);
            AccountViewDTO accountViewDTO= new AccountViewDTO(acc1.getId(), acc1.getEmail() , acc1.getAuthorities());
            return accountViewDTO;

    }

    @PutMapping(value="/profile/{user_id}/update-authorities",produces ="application/json",consumes ="application/json")
    @ApiResponse(responseCode="200",description = "Authorities Updated")
    @ApiResponse(responseCode="400",description = "Invalid User")
    @ApiResponse(responseCode="403",description = "Token Error")
    @Operation(summary="Update Profile Authority")
    @SecurityRequirement(name="Album Api")
    public ResponseEntity<AccountViewDTO> update_auth( @Valid @RequestBody AuthoritiesDTO authoritiesDTO,@PathVariable long user_id){

        Optional<Account> acc= accountService.findById(user_id);

        if(acc.isPresent()){

            Account acc1= acc.get();
            acc1.setAuthorities(authoritiesDTO.getAuthorities());
            accountService.save(acc1);
            AccountViewDTO accountViewDTO= new AccountViewDTO(acc1.getId(), acc1.getEmail() , acc1.getAuthorities());
            return ResponseEntity.ok(accountViewDTO);
        }

        return new ResponseEntity<>(new AccountViewDTO(),HttpStatus.BAD_REQUEST);

    }


    @DeleteMapping(value="/profile/delete")
    @ApiResponse(responseCode="200",description = "Password Deleted")
    @ApiResponse(responseCode="401",description = "Token Missing")
    @ApiResponse(responseCode="403",description = "Token Error")
    @Operation(summary="Delete Profile")
    @SecurityRequirement(name="Album Api")
    public ResponseEntity<String> delete_auth(Authentication authentication){

        String email= authentication.getName();  // it return the username or email (or anything) ie used as usename
        Optional<Account> acc= accountService.findByEmail(email);
           
        if(acc.isPresent()){
            Account acc1= acc.get();
            accountService.deleteById(acc1.getId());
            return ResponseEntity.ok("Account Deleted");
        }

        return new ResponseEntity<>(("Something Went Wrong!"),HttpStatus.BAD_REQUEST);

    }



}
