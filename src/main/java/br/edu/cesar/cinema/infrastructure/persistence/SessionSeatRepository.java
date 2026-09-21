package br.edu.cesar.cinema.infrastructure.persistence;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.cesar.cinema.domain.SessionSeat;

public interface SessionSeatRepository extends JpaRepository<SessionSeat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select seat from SessionSeat seat
            where seat.movieSession.id = :sessionId
              and seat.seatCode in :seatCodes
            order by seat.id
            """)
    List<SessionSeat> lockBySessionIdAndSeatCodes(
            @Param("sessionId") Long sessionId,
            @Param("seatCodes") Collection<String> seatCodes
    );
}
