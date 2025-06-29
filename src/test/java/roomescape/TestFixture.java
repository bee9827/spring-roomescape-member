package roomescape;

import org.springframework.stereotype.Component;
import roomescape.reservationTime.service.dto.command.ReservationTimeCreateCommand;
import roomescape.theme.service.dto.command.ThemeCreateCommand;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestFixture {
    public static final LocalDate DEFAULT_DATE = LocalDate.now().plusDays(1);

    public static List<ThemeCreateCommand> createThemeCommands() {
        List<ThemeCreateCommand> commands = new ArrayList<>();

        commands.add(ThemeCreateCommand.builder()
                .name("축배의 방")
                .description("테스트용")
                .thumbnail("url")
                .build());
        commands.add(ThemeCreateCommand.builder()
                .name("저주의 방")
                .description("테스트용")
                .thumbnail("url")
                .build());
        commands.add(ThemeCreateCommand.builder()
                .name("승리의 방")
                .description("테스트용")
                .thumbnail("url")
                .build());

        return commands;
    }

    public static List<ReservationTimeCreateCommand> createReservationTimeCommands() {
        List<ReservationTimeCreateCommand> commands = new ArrayList<>();

        commands.add(ReservationTimeCreateCommand.builder()
                .startAt(LocalTime.of(10, 0))
                .build());
        commands.add(ReservationTimeCreateCommand.builder()
                .startAt(LocalTime.of(13, 0))
                .build());
        commands.add(ReservationTimeCreateCommand.builder()
                .startAt(LocalTime.of(16, 0))
                .build());

        return commands;
    }

}
