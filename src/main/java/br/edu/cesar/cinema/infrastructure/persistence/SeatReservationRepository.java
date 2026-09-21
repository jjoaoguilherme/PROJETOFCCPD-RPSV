package br.edu.cesar.cinema.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.cesar.cinema.domain.SeatReservation;

public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {
}
