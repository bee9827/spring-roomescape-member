package roomescape.reservation.domain;

import jakarta.validation.constraints.FutureOrPresent;
import roomescape.reservation.controller.dto.request.ReservationSearchCriteria;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    Reservation findById(Long id);

    Reservation save(Reservation reservation);

    void delete(Reservation reservation);

    List<Reservation> findAll();

    boolean existsById(Long id);

    void deleteById(Long id);

    boolean isDuplicated(Theme theme, LocalDate date, ReservationTime reservationTime);

    List<Reservation> findByCriteria(ReservationSearchCriteria reservationSearchCriteria);

}
