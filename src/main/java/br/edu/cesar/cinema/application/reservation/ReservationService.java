package br.edu.cesar.cinema.application.reservation;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.cesar.cinema.domain.MovieSession;
import br.edu.cesar.cinema.domain.SeatReservation;
import br.edu.cesar.cinema.domain.SessionSeat;
import br.edu.cesar.cinema.infrastructure.persistence.MovieSessionRepository;
import br.edu.cesar.cinema.infrastructure.persistence.SeatReservationRepository;
import br.edu.cesar.cinema.infrastructure.persistence.SessionSeatRepository;

@Service
public class ReservationService {

    private final MovieSessionRepository movieSessionRepository;
    private final SessionSeatRepository sessionSeatRepository;
    private final SeatReservationRepository seatReservationRepository;

    public ReservationService(
            MovieSessionRepository movieSessionRepository,
            SessionSeatRepository sessionSeatRepository,
            SeatReservationRepository seatReservationRepository
    ) {
        this.movieSessionRepository = movieSessionRepository;
        this.sessionSeatRepository = sessionSeatRepository;
        this.seatReservationRepository = seatReservationRepository;
    }

    @Transactional
    public ReservationResult reserve(ReservationCommand command) {
        validate(command);

        MovieSession session = movieSessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new SessionNotFoundException(command.sessionId()));

        List<String> requestedSeats = command.seatCodes().stream()
                .map(String::trim)
                .sorted(Comparator.naturalOrder())
                .toList();

        List<SessionSeat> lockedSeats = sessionSeatRepository.lockBySessionIdAndSeatCodes(
                session.getId(),
                requestedSeats
        );

        if (lockedSeats.size() != requestedSeats.size() || lockedSeats.stream().anyMatch(seat -> !seat.isAvailable())) {
            throw new SeatsUnavailableException("Um ou mais assentos solicitados nao estao disponiveis.");
        }

        lockedSeats.forEach(SessionSeat::reserve);

        SeatReservation reservation = seatReservationRepository.save(
                new SeatReservation(session, command.customerName().trim(), new LinkedHashSet<>(lockedSeats))
        );

        return new ReservationResult(
                reservation.getId(),
                lockedSeats.stream().map(SessionSeat::getSeatCode).toList()
        );
    }

    private void validate(ReservationCommand command) {
        if (command == null || command.sessionId() == null) {
            throw new IllegalArgumentException("A sessao e obrigatoria.");
        }

        if (command.customerName() == null || command.customerName().isBlank()) {
            throw new IllegalArgumentException("O nome do cliente e obrigatorio.");
        }

        if (command.seatCodes() == null || command.seatCodes().isEmpty()) {
            throw new IllegalArgumentException("Informe pelo menos um assento.");
        }

        Set<String> normalizedSeats = command.seatCodes().stream()
                .filter(code -> code != null && !code.isBlank())
                .map(String::trim)
                .collect(java.util.stream.Collectors.toSet());

        if (normalizedSeats.size() != command.seatCodes().size()) {
            throw new IllegalArgumentException("Os assentos informados devem ser validos e distintos.");
        }
    }
}
