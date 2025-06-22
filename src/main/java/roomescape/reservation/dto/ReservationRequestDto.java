package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;

@Builder
public record ReservationRequestDto(
        @NotNull
        @NotBlank(message = "이름은 공백일 수 없습니다.")
        String name,

        @NotNull(message = "날짜는 공백일 수 없습니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "테마는 공백일 수 없습니다.")
        Long themeId,

        @NotNull(message = "시간은 공백일 수 없습니다.")
        @JsonProperty("timeId")
        Long reservationTimeId) {
    public Reservation toEntity(Theme theme, ReservationTime reservationTime) {
        return Reservation.builder()
                .name(name)
                .date(date)
                .theme(theme)
                .reservationTime(reservationTime)
                .build();
    }
}
