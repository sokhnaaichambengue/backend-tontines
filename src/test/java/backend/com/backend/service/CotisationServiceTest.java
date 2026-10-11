package backend.com.backend.service;

import backend.com.backend.dto.CotisationRequest;
import backend.com.backend.entities.Membre;
import backend.com.backend.repository.CotisationRepository;
import backend.com.backend.repository.MembreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CotisationServiceTest {

    @Mock
    private CotisationRepository cotisationRepository;

    @Mock
    private MembreRepository membreRepository;

    @InjectMocks
    private CotisationService cotisationService;

    @Test
    void shouldRejectVersementThatExceedsRemainingAmount() {
        Membre membre = new Membre();
        membre.setId(1L);
        membre.setMontantCotisation(1000.0);
        membre.setTotalVerse(200.0);

        when(membreRepository.findById(1L)).thenReturn(Optional.of(membre));

        CotisationRequest request = new CotisationRequest();
        request.setMembreId(1L);
        request.setMontant(900.0);

        assertThrows(IllegalArgumentException.class, () -> cotisationService.enregistrerVersement(request));
    }
}
