package backend.com.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class Solidarite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomMembre;

    private String telephone;

    private String message;

    private Double montant;

    private LocalDateTime dateEnvoi = LocalDateTime.now();

    // getters & setters
}
