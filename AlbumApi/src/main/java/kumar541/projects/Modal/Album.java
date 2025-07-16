package kumar541.projects.Modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
public class Album {
    
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name="account_id", referencedColumnName = "id",nullable =false)
    private Account account;


}
