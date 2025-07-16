package kumar541.projects.Controllers;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins="http://localhost:3000",maxAge = 3600)
public class HomeController {
    
    @GetMapping("/")
    @SecurityRequirement(name="Album Api")
    public String demo(){
        return"Hello World";
    }

}
