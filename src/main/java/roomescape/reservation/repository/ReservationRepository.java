package roomescape.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, CustomReservationRepository {
    boolean existsByThemeId(Long id);

    boolean existsByDateAndThemeAndReservationTime(LocalDate date, Theme theme, ReservationTime reservationTime);

    @Query("""
            SELECT r FROM Reservation r
            JOIN FETCH r.reservationTime
            JOIN FETCH r.theme
            """)
    List<Reservation> findAll();
}