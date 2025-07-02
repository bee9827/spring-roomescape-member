package roomescape.service.dto.result;

import lombok.Builder;
import roomescape.domain.Reservation;

import java.time.LocalDate;

@Builder
public record ReservationResult(
        Long id,
        MemberResult memberResult,
        ThemeResult themeResult,
        ReservationTimeResult reservationTimeResult,
        LocalDate date
) {
    public static ReservationResult from(Reservation reservation) {
        return ReservationResult.builder()
                .memberResult(MemberResult.from(reservation.getMember()))
                .themeResult(ThemeResult.from(reservation.getTheme()))
                .reservationTimeResult(ReservationTimeResult.from(reservation.getReservationTime()))
                .date(reservation.getDate())
                .build();
    }

}
