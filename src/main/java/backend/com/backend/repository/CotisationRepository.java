package backend.com.backend.repository;

import backend.com.backend.entities.Cotisation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotisationRepository extends JpaRepository<Cotisation, Long> {
}