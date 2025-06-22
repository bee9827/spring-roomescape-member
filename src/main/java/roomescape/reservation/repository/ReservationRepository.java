package roomescape.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    public boolean existsReservationByThemeId(Long id);

    boolean existsByDateAndAndReservationTimeId(LocalDate date, Long reservationTimeId);
}