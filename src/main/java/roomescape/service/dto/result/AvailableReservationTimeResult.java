package roomescape.service.dto.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import roomescape.domain.ReservationTime;

@Builder
public record AvailableReservationTimeResult(
        @JsonProperty("time")
        ReservationTimeResult reservationTimeResult,
        Boolean booked
) {
    public AvailableReservationTimeResult(ReservationTime reservationTime, Boolean booked) {
        this(ReservationTimeResult.from(reservationTime), booked);
    }
}
