package kumar541.projects.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kumar541.projects.Modal.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long> {
    
    List<Album> findByAccount_id(Long id);

}
