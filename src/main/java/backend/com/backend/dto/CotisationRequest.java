package backend.com.backend.dto;

import java.time.LocalDateTime;

public class CotisationRequest {
    private Long membreId;
    private Double montant;
    private LocalDateTime dateVersement;

    public Long getMembreId() {
        return membreId;
    }

    public void setMembreId(Long membreId) {
        this.membreId = membreId;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public LocalDateTime getDateVersement() {
        return dateVersement;
    }

    public void setDateVersement(LocalDateTime dateVersement) {
        this.dateVersement = dateVersement;
    }
}
