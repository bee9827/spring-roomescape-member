package roomescape.reservationTime.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.reservationTime.domain.AvailableReservationTime;
import roomescape.reservationTime.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long> {
    ReservationTime findReservationTimeByStartAt(LocalTime startAt);
    boolean existsReservationTimeByStartAt(@NotNull(message = "시간은 공백일 수 없습니다.") LocalTime time);

    @Query("""
            SELECT new roomescape.reservationTime.domain.AvailableReservationTime(
                rt.id,
                rt.startAt,
                EXISTS(
                    SELECT 1 FROM Reservation r
                    WHERE r.reservationTime = rt
                    AND r.theme.id = :themeId
                    AND r.date= :date
                )
            )
            FROM ReservationTime rt 
            ORDER BY rt.startAt
            """)
    List<AvailableReservationTime> findAvailable(Long themeId, LocalDate date);
}
