package roomescape;

import roomescape.service.dto.command.ReservationTimeCreateCommand;
import roomescape.service.dto.command.ThemeCreateCommand;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestFixture {
    public static final LocalDate DEFAULT_DATE = LocalDate.now().plusDays(1);
    private static final List<ThemeCreateCommand> themes = initTheme();
    private static final List<ReservationTimeCreateCommand> reservationTimes = initReservationTimes();

    public static List<ThemeCreateCommand> getThemesCreateCommand() {
        return themes;
    }

    public static List<ReservationTimeCreateCommand> getReservationTimeCommands() {
        return reservationTimes;
    }

    private static List<ThemeCreateCommand> initTheme() {
        List<ThemeCreateCommand> themes = new ArrayList<>();

        themes.add(ThemeCreateCommand.builder()
                .name("축배의 방")
                .description("테스트용")
                .thumbnail("url")
                .build());
        themes.add(ThemeCreateCommand.builder()
                .name("저주의 방")
                .description("테스트용")
                .thumbnail("url")
                .build());
        themes.add(ThemeCreateCommand.builder()
                .name("승리의 방")
                .description("테스트용")
                .thumbnail("url")
                .build());
        return themes;
    }

    private static List<ReservationTimeCreateCommand> initReservationTimes() {
        List<ReservationTimeCreateCommand> reservationTimes = new ArrayList<>();

        reservationTimes.add(ReservationTimeCreateCommand.builder()
                .startAt(LocalTime.of(10, 0))
                .build());
        reservationTimes.add(ReservationTimeCreateCommand.builder()
                .startAt(LocalTime.of(13, 0))
                .build());
        reservationTimes.add(ReservationTimeCreateCommand.builder()
                .startAt(LocalTime.of(16, 0))
                .build());
        return reservationTimes;
    }
}
