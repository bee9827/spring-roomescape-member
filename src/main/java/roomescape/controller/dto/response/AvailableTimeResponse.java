package roomescape.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import roomescape.service.dto.result.AvailableReservationTimeResult;

import java.time.LocalTime;

@Builder
public record AvailableTimeResponse(
        Long timeId,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        Boolean booked
) {
    public static AvailableTimeResponse from(AvailableReservationTimeResult availableReservationTimeResult) {
        return AvailableTimeResponse.builder()
                .timeId(availableReservationTimeResult.reservationTimeResult().id())
                .startAt(availableReservationTimeResult.reservationTimeResult().startAt())
                .booked(availableReservationTimeResult.booked())
                .build();
    }
}

