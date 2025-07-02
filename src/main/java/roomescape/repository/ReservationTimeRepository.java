package roomescape.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.service.dto.result.AvailableReservationTimeResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long> {
    ReservationTime findReservationTimeByStartAt(LocalTime startAt);

    boolean existsByStartAt(LocalTime time);

    @Query("""
            SELECT new roomescape.service.dto.result.AvailableReservationTimeResult(
                    rt,
                    EXISTS(
                        SELECT 1
                        FROM Reservation r
                        WHERE r.reservationTime = rt
                          AND r.theme.id = :themeId
                          AND r.date = :date
                    ))
                FROM ReservationTime rt
                ORDER BY rt.startAt
            """)
    List<AvailableReservationTimeResult> findAvailable(Long themeId, LocalDate date);
}
