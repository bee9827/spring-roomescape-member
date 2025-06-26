package roomescape.reservation.domain.policy;

import lombok.RequiredArgsConstructor;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.policy.Policy;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.repository.JpaReservationRepository;

@RequiredArgsConstructor
public class DuplicateReservationPolicy implements Policy<Reservation> {
    private final ReservationRepository reservationRepository;

    @Override
    public void validate(Reservation reservation) {
        if (reservationRepository.isDuplicated(
                reservation.getTheme(),reservation.getDate(), reservation.getReservationTime())) {
            throw new RestApiException(ReservationErrorStatus.DUPLICATE);
        }
    }
}
