package br.edu.cesar.cinema.domain;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "session_seats",
        uniqueConstraints = @UniqueConstraint(name = "uk_session_seat", columnNames = {"movie_session_id", "seat_code"})
)
public class SessionSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_session_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private MovieSession movieSession;

    @Column(name = "seat_code", nullable = false, length = 10)
    private String seatCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatStatus status;

    protected SessionSeat() {
    }

    public SessionSeat(MovieSession movieSession, String seatCode) {
        this.movieSession = movieSession;
        this.seatCode = seatCode;
        this.status = SeatStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public MovieSession getMovieSession() {
        return movieSession;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public void reserve() {
        if (!isAvailable()) {
            throw new IllegalStateException("O assento " + seatCode + " ja esta reservado.");
        }

        status = SeatStatus.RESERVED;
    }
}
