package roomescape.reservationTime.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.reservationTime.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @RequestBody
            @Valid
            ReservationTimeRequest reservationTimeRequest) {
        ReservationTime request = reservationTimeRequest.toEntity();
        ReservationTime savedTime = reservationTimeService.save(request);
        ReservationTimeResponse responseDto = ReservationTimeResponse.from(savedTime);
        return ResponseEntity.created(URI.create("/times")).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        //잘못된 아이디 전달
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok(reservationTimeResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        //잘못된 아이디 전달 했을 경우 예외 처리 필요.
        reservationTimeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }


}
