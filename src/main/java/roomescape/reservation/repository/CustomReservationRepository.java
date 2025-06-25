package roomescape.reservation.repository;

import roomescape.reservation.domain.Reservation;

import java.util.List;
import java.util.Map;

public interface CustomReservationRepository {
    List<Reservation> findAllByFilter(Map<String, Object> filterParamsWithoutNull);
}
