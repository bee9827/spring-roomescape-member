package roomescape.reservationTime.service.dto.command;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.reservationTime.domain.ReservationTime;

import java.time.LocalTime;

@Builder
public record ReservationTimeCreateCommand(
        @NotNull(message = "시작 시간은 공백일 수 없습니다.")
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime startAt
        ) {

        public ReservationTime toEntity() {
                return new ReservationTime(
                        startAt
                );
        }
}
