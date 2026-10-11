package backend.com.backend.service;

import backend.com.backend.dto.DiamraParticipationRequest;
import backend.com.backend.dto.DiamraResult;
import backend.com.backend.entities.Diamra;
import backend.com.backend.entities.DiamraParticipation;
import backend.com.backend.repository.DiamraParticipationRepository;
import backend.com.backend.repository.DiamraRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class DiamraService {

    private final DiamraRepository diamraRepository;
    private final DiamraParticipationRepository participationRepository;

    public DiamraService(DiamraRepository diamraRepository, DiamraParticipationRepository participationRepository) {
        this.diamraRepository = diamraRepository;
        this.participationRepository = participationRepository;
    }

    public List<DiamraParticipation> getParticipants() {
        return participationRepository.findAllByOrderByIdAsc();
    }

    public DiamraParticipation participer(DiamraParticipationRequest request) {
        DiamraParticipation participation = new DiamraParticipation();
        participation.setNom(request.getNom());
        participation.setTelephone(request.getTelephone());
        participation.setNature(request.getNature());
        participation.setMontant(request.getMontant());
        return participationRepository.save(participation);
    }

    public Diamra getProchainDiamra() {
        return diamraRepository.findTopByOrderByDateTirageAsc().orElseGet(() -> {
            Diamra diamra = new Diamra();
            diamra.setNature("Lot à définir");
            diamra.setDateTirage(LocalDate.now().plusDays(7));
            return diamra;
        });
    }

    public DiamraResult lancerTirage() {
        List<DiamraParticipation> participants = participationRepository.findAllByOrderByIdAsc();
        if (participants.isEmpty()) {
            throw new IllegalStateException("Aucun participant pour le tirage");
        }

        Random random = new Random();
        DiamraParticipation gagnant = participants.get(random.nextInt(participants.size()));

        Diamra diamra = new Diamra();
        diamra.setNature(gagnant.getNature());
        diamra.setDateTirage(LocalDate.now());
        diamra.setGagnant(gagnant.getNom());
        diamraRepository.save(diamra);

        return new DiamraResult(gagnant.getNom(), gagnant.getNature());
    }
}
