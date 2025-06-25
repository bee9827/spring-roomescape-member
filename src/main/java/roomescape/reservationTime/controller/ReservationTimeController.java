package roomescape.reservationTime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationTime.service.ReservationTimeService;
import roomescape.reservationTime.service.dto.AvailableTimeOutput;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeOutput>> available(
            @RequestParam final Long themeId,
            @RequestParam final LocalDate date
    ) {
        List<AvailableTimeOutput> availableTimeOutputs = reservationTimeService.findAvailable(themeId, date);

        return ResponseEntity.ok(availableTimeOutputs);
    }
}
