package kumar541.projects.payload.Album;

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
public class PhotoDTO {
    
    private Long id;

    private String Name;
    
    private String description;

    private String originalFileName;

    private String download_link;


}
