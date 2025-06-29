package roomescape.reservationTime.service.dto.result;

import lombok.Builder;
import roomescape.reservationTime.domain.ReservationTime;

import java.time.LocalTime;

@Builder
public record ReservationTimeResult(
        Long id,
        LocalTime startAt
) {
    public static ReservationTimeResult from(ReservationTime reservationTime) {
        return ReservationTimeResult.builder()
                .id(reservationTime.getId())
                .startAt(reservationTime.getStartAt())
                .build();
    }
}
