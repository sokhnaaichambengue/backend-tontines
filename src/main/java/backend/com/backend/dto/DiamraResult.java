package backend.com.backend.dto;

public class DiamraResult {
    private String gagnant;
    private String nature;

    public DiamraResult(String gagnant, String nature) {
        this.gagnant = gagnant;
        this.nature = nature;
    }

    public String getGagnant() {
        return gagnant;
    }

    public String getNature() {
        return nature;
    }
}
