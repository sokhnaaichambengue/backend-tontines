
package backend.com.backend.repository;

import backend.com.backend.entities.Membre;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembreRepository
        extends JpaRepository<Membre, Long> {

    Optional<Membre> findFirstByTelephoneAndActifTrue(String telephone);

    Optional<Membre> findFirstByCodeAndActifTrue(String code);

}