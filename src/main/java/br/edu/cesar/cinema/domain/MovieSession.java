package br.edu.cesar.cinema.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movie_sessions")
public class MovieSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String movieTitle;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    protected MovieSession() {
    }

    public MovieSession(String movieTitle, LocalDateTime startsAt) {
        this.movieTitle = movieTitle;
        this.startsAt = startsAt;
    }

    public Long getId() {
        return id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }
}
