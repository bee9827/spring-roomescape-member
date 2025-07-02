package roomescape.domain;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record AvailableReservationTime(
        Long reservationTimeId,
        LocalTime startAt,
        Boolean booked
) {
}
