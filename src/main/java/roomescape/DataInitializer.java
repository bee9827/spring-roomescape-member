package roomescape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.service.MemberService;
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
    private final MemberService memberService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ReservationTime time = ReservationTime.builder()
                .startAt(LocalTime.of(10, 0))
                .build();

        ReservationTime time2 = ReservationTime.builder()
                .startAt(LocalTime.of(13, 0))
                .build();

        ReservationTime time3 = ReservationTime.builder()
                .startAt(LocalTime.of(16, 0))
                .build();

        Theme theme = Theme.builder()
                .name("저주의 방")
                .description("테스트용")
                .thumbnail("url")
                .build();

        Theme theme2 = Theme.builder()
                .name("승리의 방")
                .description("테스트용")
                .thumbnail("url")
                .build();

        Theme theme3 = Theme.builder()
                .name("축배의 방")
                .description("테스트용")
                .thumbnail("url")
                .build();

        Reservation reservation = Reservation.builder()
                .name("이름")
                .theme(theme)
                .date(LocalDate.now().plusDays(1))
                .reservationTime(time)
                .build();

        Member member = Member.builder()
                .name("용성")
                .email("ehfrhfo9494@naver.com")
                .password("1007")
                .build();

        Member admin = Member.builder()
                .name("admin")
                .email("admin@naver.com")
                .password("1007")
                .role(Role.ADMIN)
                .build();

        memberService.save(member);
        memberService.save(admin);
        reservationTimeService.save(time);
        reservationTimeService.save(time2);
        reservationTimeService.save(time3);
        themeService.save(theme);
        themeService.save(theme2);
        themeService.save(theme3);
        reservationService.save(reservation);
    }
}
