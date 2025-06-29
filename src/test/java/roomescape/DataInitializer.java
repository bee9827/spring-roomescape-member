package roomescape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.service.MemberService;
import roomescape.member.service.dto.command.MemberCreateCommand;
import roomescape.member.service.dto.result.MemberResult;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.command.ReservationCreateCommand;
import roomescape.reservationTime.controller.dto.response.ReservationTimeResponse;
import roomescape.reservationTime.service.ReservationTimeService;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Profile("test")
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final MemberService memberService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        MemberCreateCommand member = MemberCreateCommand.builder()
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

        MemberResult savedMember = memberService.save(member);

        List<ReservationTimeResponse> reservationTimeResponses = new ArrayList<>();
        List<ThemeResponse> themeResponses = new ArrayList<>();

        TestFixture.createReservationTimeCommands()
                .forEach(command -> {
                    reservationTimeResponses.add(reservationTimeService.save(command));
                });
        TestFixture.createThemeCommands()
                .forEach(command -> {
                    themeResponses.add(themeService.save(command));
                });

        for (int i = 0; i < 3; i++) {
            reservationService.save(
                    ReservationCreateCommand.builder()
                            .memberId(savedMember.id())
                            .themeId(themeResponses.get(i).id())
                            .reservationTimeId(reservationTimeResponses.get(i).id())
                            .date(TestFixture.DEFAULT_DATE)
                            .build());
        }
    }
}
