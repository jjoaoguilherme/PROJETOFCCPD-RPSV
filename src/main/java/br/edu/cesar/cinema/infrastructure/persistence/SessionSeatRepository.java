package br.edu.cesar.cinema.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.cesar.cinema.domain.SessionSeat;

public interface SessionSeatRepository extends JpaRepository<SessionSeat, Long> {
}
