package backend.com.backend.service;

import backend.com.backend.dto.DiamraParticipationRequest;
import backend.com.backend.dto.DiamraResult;
import backend.com.backend.entities.Diamra;
import backend.com.backend.entities.DiamraParticipation;
import backend.com.backend.repository.DiamraParticipationRepository;
import backend.com.backend.repository.DiamraRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DiamraServiceTest {

    @Test
    void shouldCreateParticipationAndSelectWinner() {
        DiamraRepository diamraRepository = mock(DiamraRepository.class);
        DiamraParticipationRepository participationRepository = mock(DiamraParticipationRepository.class);

        when(diamraRepository.findTopByOrderByDateTirageAsc()).thenReturn(Optional.empty());
        when(diamraRepository.save(any(Diamra.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(participationRepository.save(any(DiamraParticipation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DiamraParticipation first = new DiamraParticipation();
        first.setId(1L);
        first.setNom("Awa");
        first.setTelephone("772000000");
        first.setNature("Savon");
        first.setMontant(5000.0);

        DiamraParticipation second = new DiamraParticipation();
        second.setId(2L);
        second.setNom("Moussa");
        second.setTelephone("775000000");
        second.setNature("Huile");
        second.setMontant(10000.0);

        when(participationRepository.findAllByOrderByIdAsc()).thenReturn(List.of(first, second));

        DiamraService service = new DiamraService(diamraRepository, participationRepository);

        DiamraParticipationRequest request = new DiamraParticipationRequest();
        request.setNom("Awa");
        request.setTelephone("772000000");
        request.setNature("Savon");
        request.setMontant(5000.0);

        DiamraParticipation participation = service.participer(request);
        assertEquals("Savon", participation.getNature());
        assertEquals(5000.0, participation.getMontant());

        DiamraResult result = service.lancerTirage();
        assertNotNull(result.getGagnant());
        assertNotNull(result.getNature());
    }
}
