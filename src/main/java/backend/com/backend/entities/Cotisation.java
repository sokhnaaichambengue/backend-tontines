package backend.com.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Cotisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membre_id")
    private Membre membre;

    private String nom;

    private String telephone;

    private Double montant;

    @Column(name = "date_versement")
    private LocalDateTime dateVersement = LocalDateTime.now();

    private LocalDate dateLimite;

    private boolean retard = false;

    private Double amende = 0.0;
}