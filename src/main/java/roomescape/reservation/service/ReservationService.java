package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;


    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation save(ReservationRequestDto reservationRequestDto) {
        validateDuplicateDateAndTime(reservationRequestDto);

        ReservationTime time = reservationTimeRepository.findById(reservationRequestDto.reservationTimeId())
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(reservationRequestDto.themeId())
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.THEME_NOT_FOUND));

        Reservation requestReservation = reservationRequestDto.toEntity(theme, time);

        return save(requestReservation);
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.NOT_FOUND));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll()
                .stream()
                .toList();
    }

    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RestApiException(ReservationErrorStatus.NOT_FOUND);
        }

        reservationRepository.deleteById(id);
    }


    private void validateDuplicateDateAndTime(ReservationRequestDto reservationRequestDto) {
        LocalDate date = reservationRequestDto.date();
        if (reservationRepository.existsByDateAndReservationTimeId(date, reservationRequestDto.reservationTimeId())) {
            throw new RestApiException(ReservationErrorStatus.DUPLICATE_DATE_TIME);
        }
    }
}
