package roomescape.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByThemeId(Long id);

    @Query("""
            SELECT r FROM Reservation r
            JOIN FETCH r.reservationTime
            JOIN FETCH r.theme
            """)
    List<Reservation> findAll();

    @Query("""
            SELECT EXISTS(
                SELECT 1 FROM Reservation r
                WHERE r.theme = :theme
                AND r.date = :date
                AND r.reservationTime = :reservationTime
            )
            """)
    boolean isDuplicated(Theme theme, LocalDate date, ReservationTime reservationTime);

    @Query("""
            SELECT r FROM Reservation r
            where :themeId IS NULL OR r.theme.id = :themeId
            AND :memberId IS NULL OR r.member.id = :memberId
            AND(
                (:dateFrom IS NULL AND :dateTo IS NULL)
                OR (:dateFrom IS NULL OR r.date <= :dateTo)
                OR (:dateTo IS NULL OR r.date >= :dateFrom)
                OR (r.date BETWEEN :dateFrom AND :dateTo)
            )
            """)
    List<Reservation> filter(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);
}