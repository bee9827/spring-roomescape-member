package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.DatabaseCleaner;
import roomescape.TestFixture;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.controller.dto.request.ReservationSearchCriteria;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.command.ReservationCreateCommand;
import roomescape.service.dto.result.ReservationResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    public void setUp() {
        databaseCleaner.clean();

        Member member = Member.builder()
                .name("용성")
                .email("ehfrhfo9494@naver.com")
                .password("1007")
                .build();

        memberRepository.save(member);

        List<ReservationTime> reservationTimeResponses = new ArrayList<>();
        List<Theme> themeResults = new ArrayList<>();

        TestFixture.getReservationTimeCommands()
                .forEach(command -> {
                    reservationTimeResponses.add(reservationTimeRepository.save(command.toEntity()));
                });
        TestFixture.getThemesCreateCommand()
                .forEach(command -> {
                    themeResults.add(themeRepository.save(command.toEntity()));
                });

        for (int i = 0; i < 3; i++) {
            reservationService.save(
                    ReservationCreateCommand.builder()
                            .memberId(member.getId())
                            .themeId(themeResults.get(i).getId())
                            .reservationTimeId(reservationTimeResponses.get(i).getId())
                            .date(TestFixture.DEFAULT_DATE)
                            .build());
        }

    }

    @Nested
    @DisplayName("필터링 검색")
    class SearchByCriteria {

        @Test
        @DisplayName("조건이 없을때 모든 목록을 반환 한다.")
        void NoCriteria() {
            //when
            ReservationSearchCriteria reservationSearchCriteria = ReservationSearchCriteria.builder()
                    .build();   // 검색 조건이 없을때
            List<ReservationResult> reservationResults = reservationService.searchByCriteria(reservationSearchCriteria);

            assertAll(
                    () -> assertThat(reservationResults).hasSize(reservationService.findAll().size())
            );
        }

        @Test
        @DisplayName("조건이 있을경우 필터링하여 목록을 반환한다.")
        void filter() {
            ReservationSearchCriteria reservationSearchCriteria = ReservationSearchCriteria.builder()
                    .themeId(1L)
                    .build();   // 검색 조건이 없을때
            List<ReservationResult> reservationResults = reservationService.searchByCriteria(reservationSearchCriteria);

            List<ReservationResult> expectedResults = reservationService.findAll()
                    .stream()
                    .filter(resp -> resp.themeResult().id().equals(1L))
                    .toList();


            assertAll(
                    () -> assertThat(reservationResults).hasSize(expectedResults.size())
            );

        }
    }


    @Nested
    @DisplayName("저장")
    class Save {
        ReservationCreateCommand reservationCreateCommand = ReservationCreateCommand.builder()
                .date(TestFixture.DEFAULT_DATE.plusDays(1))
                .memberId(1L)
                .themeId(1L)
                .reservationTimeId(1L)
                .build();

        @Test
        @DisplayName("CreateCommand: 저장에 성공한다.")
        void save() {
            ReservationResult saved = reservationService.save(reservationCreateCommand);

            assertThat(saved.date()).isEqualTo(reservationCreateCommand.date());
        }

        @Test
        @DisplayName("예외: 저장된 Time이 없다면 예외를 던진다.")
        void reservationTimeNotFound() {
            reservationTimeRepository.deleteAll();

            assertThatThrownBy(() -> reservationService.save(reservationCreateCommand))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationErrorStatus.TIME_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("예외: 저장된 Theme이 없다면 예외를 던진다.")
        void themeNotFound() {
            themeRepository.deleteAll();

            assertThatThrownBy(() -> reservationService.save(reservationCreateCommand))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationErrorStatus.THEME_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("예외: 저장된 Member가 없다면 예외를 던진다.")
        void memberNotFound() {
            memberRepository.deleteAll();

            assertThatThrownBy(() -> reservationService.save(reservationCreateCommand))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationErrorStatus.MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("예외: 중복 이면 예외를 발생 시킨다.")
        void validate() {
            reservationService.save(reservationCreateCommand);

            assertThatThrownBy(() -> reservationService.save(reservationCreateCommand))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationErrorStatus.DUPLICATE.getMessage());
        }
    }

    @Nested
    @DisplayName("findAll: ")
    class FindAll {
        @Test
        @DisplayName("성공하면 ReservtaionResult를 반환한다.")
        public void success(){
            List<ReservationResult> reservationResults = reservationService.findAll();

            List<Reservation> saved = reservationRepository.findAll();

            assertThat(reservationResults).hasSize(saved.size());
        }

    }

    @Nested
    @DisplayName("삭제")
    class deleteById {
        @Test
        @DisplayName("성공한다")
        void success() {
            Reservation reservation = reservationRepository.findAll().getFirst();
            reservationService.deleteById(reservation.getId());

            assertThat(reservationRepository.existsById(reservation.getId()))
                    .isFalse();
        }

        @Test
        @DisplayName("예외: 없는 아이디 라면 예외를 던진다.")
        void notFoundException() {
            assertThatThrownBy(() -> reservationService.deleteById(0L))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationErrorStatus.NOT_FOUND.getMessage());
        }
    }
}