package backend.com.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "membres")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String telephone;

    private String code;

    private String type;

    @Column(name = "solde_total")
    private Double soldeTotal = 0.0;

    @Column(name = "montant_cotisation")
    private Double montantCotisation = 0.0;

    @Column(name = "total_verse")
    private Double totalVerse = 0.0;

    @Column(nullable = false)
    private boolean actif = true;

    public Membre() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getSoldeTotal() {
        return soldeTotal;
    }

    public void setSoldeTotal(Double soldeTotal) {
        this.soldeTotal = soldeTotal;
    }

    public Double getMontantCotisation() {
        return montantCotisation;
    }

    public void setMontantCotisation(Double montantCotisation) {
        this.montantCotisation = montantCotisation;
    }

    public Double getTotalVerse() {
        return totalVerse;
    }

    public void setTotalVerse(Double totalVerse) {
        this.totalVerse = totalVerse;
    }

    public Double getReste() {
        return Math.max(0.0, (montantCotisation == null ? 0.0 : montantCotisation) - (totalVerse == null ? 0.0 : totalVerse));
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
}