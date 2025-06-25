package roomescape.reservation.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import roomescape.reservation.domain.Reservation;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomReservationRepositoryImpl implements CustomReservationRepository {
    private final EntityManager em;

    @Override
    public List<Reservation> findAllByFilter(Map<String, Object> filterParamsWithoutNull) {

        StringBuilder jpql = new StringBuilder("SELECT r FROM Reservation r");
        for (Map.Entry<String, Object> entry : filterParamsWithoutNull.entrySet()) {
            jpql.append(" WHERE ").append(entry.getKey()).append(" = :").append(entry.getValue());
        }

        TypedQuery<Reservation> query = em.createQuery(jpql.toString(), Reservation.class);

        for (Map.Entry<String, Object> entry : filterParamsWithoutNull.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }

}
