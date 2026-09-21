package br.edu.cesar.cinema.web;

import java.time.LocalDateTime;
import java.util.List;

public record SessionResponse(Long id, String movieTitle, LocalDateTime startsAt, List<SeatResponse> seats) {
}
