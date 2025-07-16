package kumar541.projects.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileDTO {
    
    private Long id;
    private String email;
    private String authorities;

}
