package br.edu.cesar.cinema.application.reservation;

import java.util.List;

public record ReservationResult(Long reservationId, List<String> seatCodes) {
}
