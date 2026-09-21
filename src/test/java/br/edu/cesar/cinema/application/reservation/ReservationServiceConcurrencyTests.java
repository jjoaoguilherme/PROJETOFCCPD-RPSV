package br.edu.cesar.cinema.application.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.cesar.cinema.domain.MovieSession;
import br.edu.cesar.cinema.domain.SessionSeat;
import br.edu.cesar.cinema.infrastructure.persistence.MovieSessionRepository;
import br.edu.cesar.cinema.infrastructure.persistence.SeatReservationRepository;
import br.edu.cesar.cinema.infrastructure.persistence.SessionSeatRepository;

@SpringBootTest
class ReservationServiceConcurrencyTests {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MovieSessionRepository movieSessionRepository;

    @Autowired
    private SessionSeatRepository sessionSeatRepository;

    @Autowired
    private SeatReservationRepository seatReservationRepository;

    @AfterEach
    void clearDatabase() {
        seatReservationRepository.deleteAll();
        sessionSeatRepository.deleteAll();
        movieSessionRepository.deleteAll();
    }

    @Test
    void confirmsOnlyOneReservationWhenTwoClientsCompeteForTheSameSeat() throws Exception {
        MovieSession session = movieSessionRepository.save(
                new MovieSession("Disputa pelo Assento", LocalDateTime.of(2026, 10, 12, 20, 0))
        );
        sessionSeatRepository.saveAndFlush(new SessionSeat(session, "A1"));

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            Future<Boolean> firstAttempt = executor.submit(reservationAttempt(session.getId(), "Cliente 1", ready, start));
            Future<Boolean> secondAttempt = executor.submit(reservationAttempt(session.getId(), "Cliente 2", ready, start));

            ready.await();
            start.countDown();

            long confirmations = List.of(firstAttempt.get(), secondAttempt.get()).stream()
                    .filter(Boolean::booleanValue)
                    .count();

            assertThat(confirmations).isEqualTo(1);
            assertThat(seatReservationRepository.count()).isEqualTo(1);
        } finally {
            executor.shutdownNow();
        }
    }

    private Callable<Boolean> reservationAttempt(
            Long sessionId,
            String customerName,
            CountDownLatch ready,
            CountDownLatch start
    ) {
        return () -> {
            ready.countDown();
            start.await();

            try {
                reservationService.reserve(new ReservationCommand(sessionId, customerName, List.of("A1")));
                return true;
            } catch (SeatsUnavailableException exception) {
                return false;
            }
        };
    }
}
