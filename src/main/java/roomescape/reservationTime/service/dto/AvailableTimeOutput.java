package roomescape.reservationTime.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import roomescape.reservationTime.domain.AvailableReservationTime;

import java.time.LocalTime;

@Builder
public record AvailableTimeOutput(
        Long timeId,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        Boolean booked
) {
    public static AvailableTimeOutput from(AvailableReservationTime availableReservationTime) {
        return AvailableTimeOutput.builder()
                .timeId(availableReservationTime.reservationTimeId())
                .startAt(availableReservationTime.startAt())
                .booked(availableReservationTime.booked())
                .build();
    }
}

