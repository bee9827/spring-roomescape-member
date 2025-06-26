package roomescape.reservation.domain.policy;

import lombok.RequiredArgsConstructor;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.policy.Policy;
import roomescape.reservation.domain.Reservation;

import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PastDateTimeReservationPolicy implements Policy<Reservation> {
    private final Clock clock;

    @Override
    public void validate(Reservation reservation) {
        LocalDateTime reservationDateTime = reservation.getDateTime();
        LocalDateTime now = LocalDateTime.now(clock);

        if (now.isAfter(reservationDateTime)) {
            throw new RestApiException(ReservationErrorStatus.PAST_DATE_TIME);
        }
    }
}
