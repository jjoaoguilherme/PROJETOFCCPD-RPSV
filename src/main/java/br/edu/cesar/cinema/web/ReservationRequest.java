package br.edu.cesar.cinema.web;

import java.util.List;

public record ReservationRequest(String customerName, List<String> seatCodes) {
}
