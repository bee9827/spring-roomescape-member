package roomescape.policy;

import roomescape.reservation.domain.Reservation;

public interface Policy<T> {
    void validate(T t);
}
