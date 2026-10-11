package backend.com.backend.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "epargnes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Epargne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "membre_id", nullable = false)
    private Membre membre;

    private String type; // "ENTREE" ou "SORTIE"

    private Double montant;

    private String description;

    // On utilise String pour la date pour matcher exactement le format "YYYY-MM-DD" du frontend
    private String date; 

    public Epargne() {}

    // --- GETTERS ET SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Membre getMembre() { return membre; }
    public void setMembre(Membre membre) { this.membre = membre; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}