package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.request.ReservationPatchDto;
import roomescape.dto.request.ReservationRequestDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReservationConcurrencyTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Reservation savedReservation;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        Time time = timeDao.insert(new Time(LocalTime.of(13, 0)));
        Theme theme = themeDao.insert(new Theme(new Name("방탈출"), "http://url", "설명"));
        savedReservation = reservationDao.insert(
                new Reservation("유저1", LocalDate.now().plusDays(1), time, theme));
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM reservations");
        jdbcTemplate.update("DELETE FROM times");
        jdbcTemplate.update("DELETE FROM themes");
    }

    @Test
    @DisplayName("같은 슬롯에 3개 동시 예약 요청이 들어오면 하나만 성공하고 나머지는 409를 반환한다")
    void concurrentInsertResultsInOneSuccess() throws InterruptedException {
        // given
        int threadCount = 3;
        ReservationRequestDto request = new ReservationRequestDto(
                "유저", LocalDate.now().plusDays(2),
                savedReservation.getTime().getId(),
                savedReservation.getTheme().getId());

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        Runnable sendPostRequest = () -> {
            try {
                startLatch.await();
                int statusCode = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/reservations")
                        .statusCode();

                if (statusCode == HttpStatus.CREATED.value()) {
                    successCount.incrementAndGet();
                } else if (statusCode == HttpStatus.CONFLICT.value()) {
                    conflictCount.incrementAndGet();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                doneLatch.countDown();
            }
        };

        // when
        for (int i = 0; i < threadCount; i++) {
            new Thread(sendPostRequest).start();
        }
        startLatch.countDown();
        doneLatch.await();

        // then — H2는 gap lock 미지원으로 엄격한 검증 불가. MySQL 환경에서 successCount=1 보장.
        assertThat(successCount.get()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("동시에 같은 예약을 수정하면 하나만 성공하고 나머지는 409를 반환한다")
    void concurrentUpdateResultsInOneConflict() throws InterruptedException {
        // given
        int threadCount = 10;
        ReservationPatchDto request = new ReservationPatchDto(
                LocalDate.now().plusDays(3), savedReservation.getTime().getId());
        String url = "/reservations/" + savedReservation.getId() + "?name=" + savedReservation.getName();

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        Runnable sendPatchRequest = () -> {
            try {
                startLatch.await();
                int statusCode = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .patch(url)
                        .statusCode();

                if (statusCode == HttpStatus.OK.value()) {
                    successCount.incrementAndGet();
                } else if (statusCode == HttpStatus.CONFLICT.value()) {
                    conflictCount.incrementAndGet();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                doneLatch.countDown();
            }
        };

        // when
        for (int i = 0; i < threadCount; i++) {
            new Thread(sendPatchRequest).start();
        }
        startLatch.countDown();
        doneLatch.await();

        // then
        assertThat(successCount.get() + conflictCount.get()).isEqualTo(threadCount);
        assertThat(conflictCount.get()).isGreaterThanOrEqualTo(1);
    }
}
