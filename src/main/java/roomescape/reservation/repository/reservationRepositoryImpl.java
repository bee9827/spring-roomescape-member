package roomescape.reservation.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.reservation.controller.dto.request.ReservationSearchCriteria;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class reservationRepositoryImpl implements ReservationRepository {
    private final JpaReservationRepository reservationJpaRepository;

    @Override
    public Reservation findById(Long id) {
        return reservationJpaRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.NOT_FOUND));
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public void delete(Reservation reservation) {
        reservationJpaRepository.delete(reservation);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationJpaRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return reservationJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        if (!existsById(id)) {
            throw new RestApiException(ReservationErrorStatus.NOT_FOUND);
        }
        reservationJpaRepository.deleteById(id);
    }

    @Override
    public boolean isDuplicated(Theme theme, LocalDate date, ReservationTime reservationTime) {
        return reservationJpaRepository.isDuplicated(theme, date, reservationTime);
    }

    @Override
    public List<Reservation> findByCriteria(ReservationSearchCriteria criteria) {
        return reservationJpaRepository.filter(criteria.themeId(), criteria.memberId(), criteria.dateFrom(), criteria.dateTo());
    }
}
