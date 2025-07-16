package kumar541.projects.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordDTO {
    
    @Size(max = 20,min=6)
    @Schema(description="Password", example="Password@234",requiredMode=RequiredMode.REQUIRED,maxLength=20,minLength=6)
    private String password;

}
