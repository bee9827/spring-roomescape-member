package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    private Reservation savedReservation;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        Time time = timeDao.insert(new Time(LocalTime.of(13, 0)));
        Theme theme = themeDao.insert(new Theme(new Name("방탈출"), "http://url", "설명"));
        savedReservation = reservationDao.insert(
                new Reservation("유저1", LocalDate.now().plusDays(1), time, theme));
    }

    @Test
    @DisplayName("동시에 같은 예약을 수정하면 하나만 성공하고 나머지는 409를 반환한다")
    void concurrentUpdateResultsInOneConflict() throws InterruptedException {
        int threadCount = 10;
        ReservationPatchDto request = new ReservationPatchDto(
                LocalDate.now().plusDays(3), savedReservation.getTime().getId());

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        String url = "/reservations/" + savedReservation.getId()
                + "?name=" + savedReservation.getName();

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();
                    Response response = RestAssured.given()
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when()
                            .patch(url)
                            .then()
                            .extract().response();

                    if (response.statusCode() == HttpStatus.OK.value()) {
                        successCount.incrementAndGet();
                    } else if (response.statusCode() == HttpStatus.CONFLICT.value()) {
                        conflictCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        doneLatch.await();

        assertThat(successCount.get() + conflictCount.get()).isEqualTo(threadCount);
        assertThat(conflictCount.get()).isGreaterThanOrEqualTo(1);
    }
}
