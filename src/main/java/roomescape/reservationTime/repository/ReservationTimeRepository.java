package roomescape.reservationTime.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.reservationTime.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long> {
    ReservationTime findReservationTimeByStartAt(LocalTime startAt);

    boolean existsReservationTimeByStartAt(@NotNull(message = "시간은 공백일 수 없습니다.") LocalTime time);

    @Query("""
            SELECT rt FROM ReservationTime rt
            WHERE rt NOT IN(
                Select r.reservationTime FROM Reservation r
                where r.date= :date and r.theme.id = :themeId
            )
            """)
    List<ReservationTime> findAvailable(Long themeId, LocalDate date);
}
