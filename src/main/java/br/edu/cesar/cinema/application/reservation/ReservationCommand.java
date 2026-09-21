package br.edu.cesar.cinema.application.reservation;

import java.util.List;

public record ReservationCommand(Long sessionId, String customerName, List<String> seatCodes) {
}
