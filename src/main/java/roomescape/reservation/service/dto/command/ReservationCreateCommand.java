package roomescape.reservation.service.dto.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;

@Builder
public record ReservationCreateCommand(
        @NotBlank(message = "멤버 Id는 공백일 수 없습니다.")
        Long memberId,

        @NotNull(message = "날짜는 공백일 수 없습니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "테마 Id는 공백일 수 없습니다.")
        Long themeId,

        @NotNull(message = "시간 Id는 공백일 수 없습니다.")
        @JsonProperty("timeId")
        Long reservationTimeId
) {
    public Reservation toEntity(Member member, Theme theme, ReservationTime reservationTime) {
        return Reservation.builder()
                .member(member)
                .date(date)
                .theme(theme)
                .reservationTime(reservationTime)
                .build();
    }
}
