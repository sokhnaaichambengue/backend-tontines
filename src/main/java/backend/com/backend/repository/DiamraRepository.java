package backend.com.backend.repository;

import backend.com.backend.entities.Diamra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiamraRepository extends JpaRepository<Diamra, Long> {
    Optional<Diamra> findTopByOrderByDateTirageAsc();
}
