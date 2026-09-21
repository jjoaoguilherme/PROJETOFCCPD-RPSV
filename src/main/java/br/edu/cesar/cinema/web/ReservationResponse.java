package br.edu.cesar.cinema.web;

import java.util.List;

public record ReservationResponse(Long reservationId, String status, List<String> seatCodes) {
}
