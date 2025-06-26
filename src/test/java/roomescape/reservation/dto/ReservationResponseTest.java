package roomescape.reservation.dto;

import org.junit.jupiter.api.Test;
import roomescape.reservation.controller.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationResponseTest {

    @Test
    public void DateFormat() {
        ReservationResponse reservationResponse = ReservationResponse
                .builder()
                .date(LocalDate.now().plusDays(1))
                .time(LocalTime.of(10,0))
                .build();
    }
}