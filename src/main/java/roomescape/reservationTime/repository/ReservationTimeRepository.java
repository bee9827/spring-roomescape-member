package roomescape.reservationTime.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.reservationTime.domain.ReservationTime;

import java.time.LocalTime;

@Repository
public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long> {
    ReservationTime findReservationTimeByStartAt(LocalTime startAt);

    boolean existsReservationTimeByStartAt(@NotNull(message = "시간은 공백일 수 없습니다.") LocalTime time);
}
