package br.edu.cesar.cinema.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.cesar.cinema.domain.MovieSession;
import br.edu.cesar.cinema.domain.SessionSeat;
import br.edu.cesar.cinema.infrastructure.persistence.MovieSessionRepository;
import br.edu.cesar.cinema.infrastructure.persistence.SeatReservationRepository;
import br.edu.cesar.cinema.infrastructure.persistence.SessionSeatRepository;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieSessionRepository movieSessionRepository;

    @Autowired
    private SessionSeatRepository sessionSeatRepository;

    @Autowired
    private SeatReservationRepository seatReservationRepository;

    private Long sessionId;

    @BeforeEach
    void prepareSession() {
        clearDatabase();
        MovieSession session = movieSessionRepository.save(
                new MovieSession("Sessao da API", LocalDateTime.of(2026, 10, 13, 19, 30))
        );
        sessionSeatRepository.save(new SessionSeat(session, "A1"));
        sessionSeatRepository.save(new SessionSeat(session, "A2"));
        sessionId = session.getId();
    }

    @AfterEach
    void clearDatabase() {
        seatReservationRepository.deleteAll();
        sessionSeatRepository.deleteAll();
        movieSessionRepository.deleteAll();
    }

    @Test
    void listsSeatsForASession() throws Exception {
        mockMvc.perform(get("/api/sessions/{sessionId}/seats", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieTitle").value("Sessao da API"))
                .andExpect(jsonPath("$.seats[0].code").value("A1"))
                .andExpect(jsonPath("$.seats[0].status").value("AVAILABLE"));
    }

    @Test
    void confirmsReservationAndRejectsTheSameSeatAfterwards() throws Exception {
        String request = """
                {"customerName":"Ana","seatCodes":["A1"]}
                """;

        mockMvc.perform(post("/api/sessions/{sessionId}/reservations", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.seatCodes[0]").value("A1"));

        mockMvc.perform(post("/api/sessions/{sessionId}/reservations", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }
}
