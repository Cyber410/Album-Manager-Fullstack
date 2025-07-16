package kumar541.projects.payload.Album;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlbumPayloadDTO {

    @NotBlank
    @Schema(description="Album Name", example="Travel",requiredMode=RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description="Album Description", example="Mountain Travelling",requiredMode=RequiredMode.REQUIRED)
    private String description;
    
}
