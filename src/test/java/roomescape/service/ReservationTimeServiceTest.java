package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.TestFixture;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationTimeErrorStatus;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.command.ReservationTimeCreateCommand;
import roomescape.service.dto.result.AvailableReservationTimeResult;
import roomescape.service.dto.result.ReservationTimeResult;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() throws Exception {
        reservationRepository.deleteAll();
        reservationTimeRepository.deleteAll();

        List<ReservationTime> list = TestFixture.getReservationTimeCommands()
                .stream()
                .map(ReservationTimeCreateCommand::toEntity)
                .toList();

        reservationTimeRepository.saveAll(list);
    }

    @ParameterizedTest
    @CsvSource({"1", "2", "3"})
    @DisplayName("findAvailable: 예약된 타임 슬롯은 booked = true 만들어 슬롯 전체를 리턴 한다.")
    void findAvailable(Long themeId) {
        List<AvailableReservationTimeResult> availableReservationTimeResults = reservationTimeService.findAvailable(themeId, TestFixture.DEFAULT_DATE);

        List<ReservationTimeResult> bookedAvailableTIme = availableReservationTimeResults
                .stream()       //if) 예약이 이미 되어 있다면 booked = true, 모든 시간을 리턴;
                .filter(AvailableReservationTimeResult::booked)
                .map(AvailableReservationTimeResult::reservationTimeResult)
                .toList();

        List<ReservationTimeResult> bookedTimes = reservationRepository.findAll()
                .stream()
                .filter(reservation -> themeId.equals(reservation.getTheme().getId()) && TestFixture.DEFAULT_DATE.equals(reservation.getDate()))
                .map(Reservation::getReservationTime)
                .map(ReservationTimeResult::from)
                .toList();

        assertThat(availableReservationTimeResults)
                .isNotEmpty()
                .size().isEqualTo(reservationTimeRepository.findAll().size());  //모든 타임을 리턴 해야 한다.

        assertThat(bookedAvailableTIme.size()).isEqualTo(bookedTimes.size());   //booked 가 예약 된것으로 제대로 매핑되었는지.
    }

    @Test
    void findAll() {
        List<ReservationTimeResult> reservationTimeResults = reservationTimeService.findAll();
        List<ReservationTimeCreateCommand> savedReservationTimeCreateCommand = TestFixture.getReservationTimeCommands();

        assertThat(reservationTimeResults).isNotEmpty()
                .size().isEqualTo(savedReservationTimeCreateCommand.size());
    }

    @Nested
    @DisplayName("save: ")
    class Save {
        @Test
        @DisplayName("저장에 성공 한다.")
        void save() {
            LocalTime startAt = LocalTime.of(20, 0);
            ReservationTimeCreateCommand createCommand = ReservationTimeCreateCommand.builder()
                    .startAt(startAt)
                    .build();

            ReservationTimeResult savedTime = reservationTimeService.save(createCommand);
            assertThat(savedTime.startAt()).isEqualTo(startAt);
        }

        @Test
        @DisplayName("DUPLICATE_예외: start_at이 중복될 경우 예외를 던진다.")
        void duplicateStartAt() {
            ReservationTimeCreateCommand alreadySavedReservationTime = TestFixture.getReservationTimeCommands().getFirst();
            assertThatThrownBy(() -> reservationTimeService.save(alreadySavedReservationTime))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationTimeErrorStatus.DUPLICATE.getMessage());
        }
    }

    @Nested
    @DisplayName("deleteById: ")
    class DeleteById {
        @Test
        @DisplayName("성공한다.")
        void deleteById() {
            ReservationTime reservationTime = ReservationTime.builder()
                    .startAt(LocalTime.of(21, 0))
                    .build();
            reservationTimeRepository.save(reservationTime);

            reservationTimeService.deleteById(reservationTime.getId());

            assertThat(reservationRepository.existsById(reservationTime.getId())).isFalse();
        }

        @Test
        @DisplayName("NOT_FOUND 예외 : 존재하지 않는 Id입력시 예외를 던진다.")
        void notFound() {
            assertThatThrownBy(() -> reservationTimeService.deleteById(0L))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationTimeErrorStatus.NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("RESERVATION_EXIST 예외: 해당 시간에 대한 예약이 존재할 경우 예외를 던진다.")
        void reservationExist() {
            ReservationTime reservationTime = reservationTimeRepository.findAll().getFirst();

            Reservation reservation = Reservation.builder()
                    .reservationTime(reservationTime)
                    .date(TestFixture.DEFAULT_DATE)
                    .build();

            reservationRepository.save(reservation);

            assertThatThrownBy(() -> reservationTimeService.deleteById(reservationTime.getId()))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationTimeErrorStatus.RESERVATION_EXIST.getMessage());
        }
    }
}