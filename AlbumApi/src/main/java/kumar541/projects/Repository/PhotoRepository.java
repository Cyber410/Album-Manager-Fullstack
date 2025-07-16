package kumar541.projects.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kumar541.projects.Modal.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,Long> {

    List<Photo> findByAlbum_id(Long id);

} 
