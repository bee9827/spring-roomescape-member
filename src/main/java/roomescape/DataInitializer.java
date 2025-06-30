package roomescape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;
import roomescape.member.service.MemberService;
import roomescape.member.service.dto.command.MemberCreateCommand;
import roomescape.member.service.dto.result.MemberResult;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.command.ReservationCreateCommand;
import roomescape.reservationTime.controller.dto.response.ReservationTimeResponse;
import roomescape.reservationTime.service.ReservationTimeService;
import roomescape.reservationTime.service.dto.command.ReservationTimeCreateCommand;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;
import roomescape.theme.service.dto.command.ThemeCreateCommand;

import java.time.LocalDate;
import java.time.LocalTime;


@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

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

        memberRepository.save(admin);

        ReservationTimeCreateCommand time = ReservationTimeCreateCommand.builder()
                .startAt(LocalTime.of(10, 0))
                .build();

        ReservationTimeCreateCommand time2 = ReservationTimeCreateCommand.builder()
                .startAt(LocalTime.of(13, 0))
                .build();

        ReservationTimeCreateCommand time3 = ReservationTimeCreateCommand.builder()
                .startAt(LocalTime.of(16, 0))
                .build();

        ThemeCreateCommand theme = ThemeCreateCommand.builder()
                .name("The Haunted Mansion")
                .description("Solve the mysteries of the haunted mansion to escape.")
                .thumbnail("https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
                .build();

        ThemeCreateCommand theme2 = ThemeCreateCommand.builder()
                .name("Secret Agent Mission")
                .description("Complete your secret agent mission before time runs out.")
                .thumbnail("https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
                .build();

        ThemeCreateCommand theme3 = ThemeCreateCommand.builder()
                .name("Pirate''s Treasure")
                .description("Find the hidden pirate''s treasure in this thrilling adventure.")
                .thumbnail("https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
                .build();

        MemberResult savedMember = memberService.save(member);

        ReservationTimeResponse savedReservationTime = reservationTimeService.save(time);
        ReservationTimeResponse savedReservationTime2 = reservationTimeService.save(time2);
        ReservationTimeResponse savedReservationTime3 = reservationTimeService.save(time3);
        ThemeResponse savedTheme = ThemeResponse.from(themeService.save(theme));
        ThemeResponse savedTheme2 = ThemeResponse.from(themeService.save(theme2));
        ThemeResponse savedTheme3 = ThemeResponse.from(themeService.save(theme3));

        ReservationCreateCommand reservation = ReservationCreateCommand.builder()
                .memberId(savedMember.id())
                .themeId(savedTheme.id())
                .date(LocalDate.now().plusDays(1))
                .reservationTimeId(savedReservationTime.id())
                .build();

        ReservationCreateCommand reservation2 = ReservationCreateCommand.builder()
                .memberId(savedMember.id())
                .themeId(savedTheme2.id())
                .date(LocalDate.now().plusDays(1))
                .reservationTimeId(savedReservationTime2.id())
                .build();

        ReservationCreateCommand reservation3 = ReservationCreateCommand.builder()
                .memberId(savedMember.id())
                .themeId(savedTheme3.id())
                .date(LocalDate.now().plusDays(1))
                .reservationTimeId(savedReservationTime3.id())
                .build();

        reservationService.save(reservation);
        reservationService.save(reservation2);
        reservationService.save(reservation3);
    }
}
