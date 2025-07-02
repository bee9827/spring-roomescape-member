package roomescape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;
import roomescape.service.dto.command.MemberCreateCommand;
import roomescape.service.dto.command.ReservationCreateCommand;
import roomescape.service.dto.result.MemberResult;
import roomescape.service.dto.result.ReservationTimeResult;
import roomescape.service.dto.result.ThemeResult;

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

        MemberResult memberResult = memberService.save(member);

        List<ReservationTimeResult> reservationTimeResponses = new ArrayList<>();
        List<ThemeResult> themeResults = new ArrayList<>();

        TestFixture.getReservationTimeCommands()
                .forEach(command -> {
                    reservationTimeResponses.add(reservationTimeService.save(command));
                });
        TestFixture.getThemesCreateCommand()
                .forEach(command -> {
                    themeResults.add(themeService.save(command));
                });

        for (int i = 0; i < 3; i++) {
            reservationService.save(
                    ReservationCreateCommand.builder()
                            .memberId(memberResult.id())
                            .themeId(themeResults.get(i).id())
                            .reservationTimeId(reservationTimeResponses.get(i).id())
                            .date(TestFixture.DEFAULT_DATE)
                            .build());
        }
    }
}
