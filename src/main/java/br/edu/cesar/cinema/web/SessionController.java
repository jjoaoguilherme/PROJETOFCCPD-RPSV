package br.edu.cesar.cinema.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.cesar.cinema.application.reservation.ReservationCommand;
import br.edu.cesar.cinema.application.reservation.ReservationResult;
import br.edu.cesar.cinema.application.reservation.ReservationService;
import br.edu.cesar.cinema.application.reservation.SessionNotFoundException;
import br.edu.cesar.cinema.domain.MovieSession;
import br.edu.cesar.cinema.infrastructure.persistence.MovieSessionRepository;
import br.edu.cesar.cinema.infrastructure.persistence.SessionSeatRepository;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final MovieSessionRepository movieSessionRepository;
    private final SessionSeatRepository sessionSeatRepository;
    private final ReservationService reservationService;

    public SessionController(
            MovieSessionRepository movieSessionRepository,
            SessionSeatRepository sessionSeatRepository,
            ReservationService reservationService
    ) {
        this.movieSessionRepository = movieSessionRepository;
        this.sessionSeatRepository = sessionSeatRepository;
        this.reservationService = reservationService;
    }

    @GetMapping("/{sessionId}/seats")
    public SessionResponse listSeats(@PathVariable Long sessionId) {
        MovieSession session = movieSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        List<SeatResponse> seats = sessionSeatRepository.findAllByMovieSessionIdOrderBySeatCodeAsc(sessionId).stream()
                .map(seat -> new SeatResponse(seat.getSeatCode(), seat.getStatus().name()))
                .toList();

        return new SessionResponse(session.getId(), session.getMovieTitle(), session.getStartsAt(), seats);
    }

    @PostMapping("/{sessionId}/reservations")
    public ResponseEntity<ReservationResponse> reserve(
            @PathVariable Long sessionId,
            @RequestBody ReservationRequest request
    ) {
        ReservationResult result = reservationService.reserve(
                new ReservationCommand(sessionId, request.customerName(), request.seatCodes())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ReservationResponse(result.reservationId(), "CONFIRMED", result.seatCodes())
        );
    }
}
