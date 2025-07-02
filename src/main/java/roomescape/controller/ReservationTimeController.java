package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.response.AvailableTimeResponse;
import roomescape.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeResponse>> available(
            @RequestParam final Long themeId,
            @RequestParam final LocalDate date
    ) {
        List<AvailableTimeResponse> availableTimeResponses = reservationTimeService.findAvailable(themeId, date);

        return ResponseEntity.ok(availableTimeResponses);
    }
}
