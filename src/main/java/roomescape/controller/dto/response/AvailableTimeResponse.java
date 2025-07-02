package roomescape.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import roomescape.domain.AvailableReservationTime;

import java.time.LocalTime;

@Builder
public record AvailableTimeResponse(
        Long timeId,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        Boolean booked
) {
    public static AvailableTimeResponse from(AvailableReservationTime availableReservationTime) {
        return AvailableTimeResponse.builder()
                .timeId(availableReservationTime.reservationTimeId())
                .startAt(availableReservationTime.startAt())
                .booked(availableReservationTime.booked())
                .build();
    }
}

