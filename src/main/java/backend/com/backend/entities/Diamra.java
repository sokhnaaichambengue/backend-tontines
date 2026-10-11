package backend.com.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "diamras")
public class Diamra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nature;

    @Column(name = "date_tirage")
    private LocalDate dateTirage;

    private String gagnant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public LocalDate getDateTirage() {
        return dateTirage;
    }

    public void setDateTirage(LocalDate dateTirage) {
        this.dateTirage = dateTirage;
    }

    public String getGagnant() {
        return gagnant;
    }

    public void setGagnant(String gagnant) {
        this.gagnant = gagnant;
    }
}
