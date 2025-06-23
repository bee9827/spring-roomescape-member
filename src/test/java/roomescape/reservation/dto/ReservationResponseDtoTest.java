package roomescape.reservation.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationResponseDtoTest {

    @Test
    public void DateFormat() {
        ReservationResponseDto reservationResponseDto = ReservationResponseDto
                .builder()
                .date(LocalDate.now().plusDays(1))
                .time(LocalTime.of(10,0))
                .build();
    }
}