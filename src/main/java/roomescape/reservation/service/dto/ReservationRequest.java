package roomescape.reservation.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;

public record ReservationRequest(
        @NotNull(message = "날짜는 공백일 수 없습니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "테마 Id는 공백일 수 없습니다.")
        Long themeId,

        @NotNull(message = "시간 Id는 공백일 수 없습니다.")
        @JsonProperty("timeId")
        Long reservationTimeId
) {
    public ReservationInput toInput(final Long memberId) {
        return ReservationInput.builder()
                .memberId(memberId)
                .date(date)
                .themeId(themeId)
                .reservationTimeId(reservationTimeId)
                .build();
    }
}
