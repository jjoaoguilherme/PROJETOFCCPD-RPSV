package br.edu.cesar.cinema.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import br.edu.cesar.cinema.domain.MovieSession;
import br.edu.cesar.cinema.domain.SeatStatus;
import br.edu.cesar.cinema.domain.SessionSeat;

@DataJpaTest
class SessionSeatRepositoryTests {

    @Autowired
    private MovieSessionRepository movieSessionRepository;

    @Autowired
    private SessionSeatRepository sessionSeatRepository;

    @Test
    void savesSeatsForASessionAsAvailable() {
        MovieSession session = movieSessionRepository.save(
                new MovieSession("Teste de Concorrencia", LocalDateTime.of(2026, 10, 11, 20, 0))
        );

        SessionSeat seat = sessionSeatRepository.save(new SessionSeat(session, "C1"));

        assertThat(seat.getId()).isNotNull();
        assertThat(seat.getStatus()).isEqualTo(SeatStatus.AVAILABLE);
        assertThat(seat.getMovieSession().getId()).isEqualTo(session.getId());
    }

    @Test
    void doesNotAllowTheSameSeatTwiceInOneSession() {
        MovieSession session = movieSessionRepository.save(
                new MovieSession("Teste de Integridade", LocalDateTime.of(2026, 10, 11, 21, 0))
        );

        sessionSeatRepository.saveAndFlush(new SessionSeat(session, "D1"));

        assertThatThrownBy(() -> sessionSeatRepository.saveAndFlush(new SessionSeat(session, "D1")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
