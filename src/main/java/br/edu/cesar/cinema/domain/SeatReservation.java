package br.edu.cesar.cinema.domain;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "seat_reservations")
public class SeatReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_session_id", nullable = false)
    private MovieSession movieSession;

    @Column(nullable = false, length = 120)
    private String customerName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @ManyToMany
    @JoinTable(
            name = "reservation_seats",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "session_seat_id")
    )
    private Set<SessionSeat> seats = new LinkedHashSet<>();

    protected SeatReservation() {
    }

    public SeatReservation(MovieSession movieSession, String customerName, Set<SessionSeat> seats) {
        this.movieSession = movieSession;
        this.customerName = customerName;
        this.createdAt = LocalDateTime.now();
        this.status = ReservationStatus.CONFIRMED;
        this.seats = new LinkedHashSet<>(seats);
    }

    public Long getId() {
        return id;
    }

    public MovieSession getMovieSession() {
        return movieSession;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Set<SessionSeat> getSeats() {
        return Set.copyOf(seats);
    }
}
