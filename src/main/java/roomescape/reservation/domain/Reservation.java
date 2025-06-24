package roomescape.reservation.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private String name;

    @FutureOrPresent
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReservationTime reservationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Builder
    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        validateNonPastDateAndTime(date, reservationTime.getStartAt());
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    private void validateNonPastDateAndTime(LocalDate date, LocalTime startAt) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(LocalDateTime.of(date, startAt))) {
            throw new RestApiException(ReservationErrorStatus.PAST_TIME);
        }
    }
}
