package br.edu.cesar.cinema.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.cesar.cinema.domain.MovieSession;

public interface MovieSessionRepository extends JpaRepository<MovieSession, Long> {
}
