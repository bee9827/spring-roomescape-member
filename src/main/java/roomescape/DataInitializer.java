package roomescape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.service.ReservationTimeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ReservationTime time = ReservationTime.builder()
                .startAt(LocalTime.of(10, 0))
                .build();

        Theme theme = Theme.builder()
                .name("테스트방")
                .description("테스트용")
                .thumbnail("url")
                .build();

        Reservation reservation = Reservation.builder()
                .name("이름")
                .theme(theme)
                .date(LocalDate.now().plusDays(1))
                .reservationTime(time)
                .build();

        reservationTimeService.save(time);
        themeService.save(theme);
        reservationService.save(reservation);
    }
}
