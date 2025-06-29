package roomescape.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.TestFixture;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.reservation.service.dto.command.ReservationCreateCommand;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ReservationValidatorTest {
    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("중복 이면 예외를 발생 시킨다.")
    void validate() {
        ReservationCreateCommand reservationCreateCommand = ReservationCreateCommand.builder()
                .memberId(1L)
                .themeId(1L)
                .reservationTimeId(1L)
                .date(TestFixture.DEFAULT_DATE)
                .build();

        assertThatThrownBy(() -> reservationService.save(reservationCreateCommand))
                .isInstanceOf(RestApiException.class)
                .hasMessage(ReservationErrorStatus.DUPLICATE.getMessage());
    }
}