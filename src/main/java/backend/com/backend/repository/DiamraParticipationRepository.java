package backend.com.backend.repository;

import backend.com.backend.entities.DiamraParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiamraParticipationRepository extends JpaRepository<DiamraParticipation, Long> {
    List<DiamraParticipation> findAllByOrderByIdAsc();
}
