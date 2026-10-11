package backend.com.backend.dto;

import lombok.Data;

@Data
public class VersementRequest {
    private Long membre_id; // Doit correspondre exactement au clé du JSON du frontend
    private Double montant;
    private String motif;
}