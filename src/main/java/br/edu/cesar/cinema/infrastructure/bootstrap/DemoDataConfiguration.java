package br.edu.cesar.cinema.infrastructure.bootstrap;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.edu.cesar.cinema.domain.MovieSession;
import br.edu.cesar.cinema.domain.SessionSeat;
import br.edu.cesar.cinema.infrastructure.persistence.MovieSessionRepository;
import br.edu.cesar.cinema.infrastructure.persistence.SessionSeatRepository;

@Configuration
public class DemoDataConfiguration {

    @Bean
    CommandLineRunner loadDemoSession(
            MovieSessionRepository movieSessionRepository,
            SessionSeatRepository sessionSeatRepository
    ) {
        return arguments -> {
            if (movieSessionRepository.count() > 0) {
                return;
            }

            MovieSession session = movieSessionRepository.save(
                    new MovieSession("Cidade em Movimento", LocalDateTime.of(2026, 10, 10, 19, 30))
            );

            sessionSeatRepository.saveAll(List.of(
                    new SessionSeat(session, "A1"),
                    new SessionSeat(session, "A2"),
                    new SessionSeat(session, "A3"),
                    new SessionSeat(session, "B1"),
                    new SessionSeat(session, "B2")
            ));
        };
    }
}
