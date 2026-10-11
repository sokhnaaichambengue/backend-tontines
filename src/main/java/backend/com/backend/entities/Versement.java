package backend.com.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "versements")
public class Versement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "membre_id", nullable = false)
    private Membre membre;

    private Double montant;
    private String motif;
    
    @Column(name = "date_versement")
    private LocalDateTime dateVersement = LocalDateTime.now();
}