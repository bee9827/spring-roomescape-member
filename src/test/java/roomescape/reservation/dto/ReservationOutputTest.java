package roomescape.reservation.dto;

import org.junit.jupiter.api.Test;
import roomescape.reservation.service.dto.ReservationOutput;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationOutputTest {

    @Test
    public void DateFormat() {
        ReservationOutput reservationOutput = ReservationOutput
                .builder()
                .date(LocalDate.now().plusDays(1))
                .time(LocalTime.of(10,0))
                .build();
    }
}