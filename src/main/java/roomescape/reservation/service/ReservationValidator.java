package roomescape.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class ReservationValidator {
    private final ReservationRepository reservationRepository;

    public void validate(Reservation reservation){
        validateDuplicate(reservation);
    }
    private void validateDuplicate(Reservation reservation) {
        if (reservationRepository.isDuplicated(reservation.getTheme(), reservation.getDate(), reservation.getReservationTime()))
            throw new RestApiException(ReservationErrorStatus.DUPLICATE);
    }
}
