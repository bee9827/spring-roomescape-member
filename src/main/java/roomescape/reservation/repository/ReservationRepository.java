package roomescape.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByThemeId(Long id);
    boolean existsByDateAndReservationTimeId(LocalDate date, Long reservationTimeId);

    @Query("""
            SELECT r FROM reservation r
            JOIN FETCH r.reservationTime
            JOIN FETCH r.theme
            """)
    List<Reservation> findAll();
}