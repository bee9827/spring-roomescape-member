package roomescape.reservationTime.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.reservationTime.service.dto.command.ReservationTimeCreateCommand;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(
        @NotNull(message = "시작 시간은 공백일 수 없습니다.")
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime startAt
) {
        public ReservationTimeCreateCommand toCommand(){
                return ReservationTimeCreateCommand.builder()
                        .startAt(startAt)
                        .build();
        }
}
