package roomescape.reservation.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.member.domain.Member;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;


@Entity(name = "Reservation")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "RESERVATION_DATE",
                        columnNames = {
                                "date",
                                "reservation_time_id",
                                "theme_id"
                        })
        }
)
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //쓰레드 세이프 하지 않다.
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @FutureOrPresent
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReservationTime reservationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Builder
    public Reservation(Member member, LocalDate date, ReservationTime reservationTime, Theme theme) {
        validatePast(LocalDateTime.of(date, reservationTime.getStartAt()));
        this.member = member;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public void validatePast(LocalDateTime dateTime) throws RestApiException {
        if (LocalDateTime.now().isAfter(dateTime)) {
            throw new RestApiException(ReservationErrorStatus.PAST_DATE_TIME);
        }
    }

}
