package roomescape.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.request.ReservationTimeCreateRequest;
import roomescape.controller.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

import static roomescape.controller.AdminReservationTimeController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class AdminReservationTimeController {
    public final static String BASE_URL = "admin/times";
    private final ReservationTimeService reservationTimeService;

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @RequestBody
            @Valid
            ReservationTimeCreateRequest reservationTimeCreateRequest) {
        ReservationTimeResponse responseDto = reservationTimeService.save(reservationTimeCreateRequest.toCommand());
        return ResponseEntity.created(URI.create("/times")).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        return ResponseEntity.ok(reservationTimeService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        //잘못된 아이디 전달 했을 경우 예외 처리 필요.
        reservationTimeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
