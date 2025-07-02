package roomescape.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomescape.TestFixture;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.repository.MemberRepository;
import roomescape.controller.dto.request.ReservationSearchCriteria;
import roomescape.service.ReservationService;
import roomescape.service.dto.command.ReservationCreateCommand;
import roomescape.service.dto.result.ReservationResult;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ReservationServiceTest {
    ReservationCreateCommand reservationCreateCommand = ReservationCreateCommand.builder()
            .date(TestFixture.DEFAULT_DATE.plusDays(1))
            .memberId(1L)
            .themeId(1L)
            .reservationTimeId(1L)
            .build();
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ThemeRepository themeRepository;

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
            ReservationCreateCommand reservationCreateCommand = ReservationCreateCommand.builder()
                    .memberId(1L)
                    .themeId(1L)
                    .reservationTimeId(1L)
                    .date(TestFixture.DEFAULT_DATE)
                    .build();

            assertThatThrownBy(() -> reservationService.save(reservationCreateCommand))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationErrorStatus.DUPLICATE.getMessage());
        }
    }

    @Nested
    @DisplayName("조회")
    class FindById {
        @Test
        @DisplayName("단일 조회에 성공한다")
        void findById() {
            ReservationResult savedReservation = reservationService.findById(1L);

            assertThat(savedReservation).isNotNull();
        }

        @Test
        @DisplayName("예외: 없는 아이디 라면 예외를 던진다.")
        void findByIdNotFound() {
            assertThatThrownBy(() -> reservationService.findById(0L))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationErrorStatus.NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("삭제")
    class deleteById {
        @Test
        @DisplayName("성공한다")
        void success() {
            reservationService.deleteById(1L);
            assertThatThrownBy(() -> reservationService.findById(1L))
                    .isInstanceOf(RestApiException.class)
                    .hasMessage(ReservationErrorStatus.NOT_FOUND.getMessage());

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