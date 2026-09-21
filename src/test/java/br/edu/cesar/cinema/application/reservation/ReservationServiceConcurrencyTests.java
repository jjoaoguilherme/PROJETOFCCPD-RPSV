package br.edu.cesar.cinema.application.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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

        int competingClients = 8;
        CountDownLatch ready = new CountDownLatch(competingClients);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(competingClients);

        try {
            List<Future<Boolean>> attempts = IntStream.rangeClosed(1, competingClients)
                    .mapToObj(client -> executor.submit(
                            reservationAttempt(session.getId(), "Cliente " + client, List.of("A1"), ready, start)
                    ))
                    .toList();

            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            long confirmations = attempts.stream()
                    .map(this::getReservationResult)
                    .filter(Boolean::booleanValue)
                    .count();

            assertThat(confirmations).isEqualTo(1);
            assertThat(seatReservationRepository.count()).isEqualTo(1);
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void confirmsReservationsForDifferentSeatsInParallel() throws Exception {
        MovieSession session = movieSessionRepository.save(
                new MovieSession("Reservas Paralelas", LocalDateTime.of(2026, 10, 12, 21, 0))
        );
        sessionSeatRepository.saveAndFlush(new SessionSeat(session, "A1"));
        sessionSeatRepository.saveAndFlush(new SessionSeat(session, "A2"));

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            Future<Boolean> firstAttempt = executor.submit(
                    reservationAttempt(session.getId(), "Cliente A", List.of("A1"), ready, start)
            );
            Future<Boolean> secondAttempt = executor.submit(
                    reservationAttempt(session.getId(), "Cliente B", List.of("A2"), ready, start)
            );

            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            assertThat(getReservationResult(firstAttempt)).isTrue();
            assertThat(getReservationResult(secondAttempt)).isTrue();
            assertThat(seatReservationRepository.count()).isEqualTo(2);
        } finally {
            executor.shutdownNow();
        }
    }

    private Callable<Boolean> reservationAttempt(
            Long sessionId,
            String customerName,
            List<String> seatCodes,
            CountDownLatch ready,
            CountDownLatch start
    ) {
        return () -> {
            ready.countDown();
            start.await();

            try {
                reservationService.reserve(new ReservationCommand(sessionId, customerName, seatCodes));
                return true;
            } catch (SeatsUnavailableException exception) {
                return false;
            }
        };
    }

    private boolean getReservationResult(Future<Boolean> attempt) {
        try {
            return attempt.get(5, TimeUnit.SECONDS);
        } catch (Exception exception) {
            throw new AssertionError("Uma tentativa de reserva nao terminou corretamente.", exception);
        }
    }
}
