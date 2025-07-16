package kumar541.projects.payload.Album;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoPayloadDTO {
    
    @NotBlank
    @Schema(description="Photo Name",example="Selfie",requiredMode=RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description="Photo Description",example="Selfie on lake",requiredMode=RequiredMode.REQUIRED)
    private String description;

}
