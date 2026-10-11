package backend.com.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomMembre;

    private String telephoneMembre;

    private String telephoneAdmin;

    @Column(length = 1000)
    private String message;

    private LocalDateTime dateEnvoi = LocalDateTime.now();

    // getters & setters
}