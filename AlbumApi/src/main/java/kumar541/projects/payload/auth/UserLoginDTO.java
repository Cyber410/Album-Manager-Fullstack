package kumar541.projects.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO{
    
    @Email
    @Schema(description="Email Address", example="abc@gmail.com",requiredMode=RequiredMode.REQUIRED)
    private String email;

    @Size(max = 20,min=6)
    @Schema(description="Password", example="Password@234",requiredMode=RequiredMode.REQUIRED,maxLength=20,minLength=6)
    private String password;

}
