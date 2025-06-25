package roomescape.reservation.service.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ReservationOutput(
        Long id,
        String name,
        String themeName,
        @JsonFormat(pattern = DATE_PATTERN) LocalDate date,
        @JsonFormat(pattern = TIME_PATTERN) LocalTime time
) {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm";


    public static ReservationOutput from(Reservation reservation) {
        return new ReservationOutput(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getReservationTime().getStartAt()
        );
    }
}
