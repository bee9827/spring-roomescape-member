package roomescape.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.controller.dto.reqeust.ReservationFilterParams;
import roomescape.reservation.controller.dto.request.ReservationCreateRequestForAdmin;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.controller.dto.response.ReservationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(AdminReservationApiController.BASE_URL)
@RequiredArgsConstructor
public class AdminReservationApiController {
    public static final String BASE_URL = "admin/reservations";

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody
            @Valid
            ReservationCreateRequestForAdmin request
    ) {
        ReservationResponse reservationResponse = reservationService.save(request.toCommand());
        URI uri = URI.create(BASE_URL + "/" + reservationResponse.id());

        return ResponseEntity.created(uri).body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> filterReservations(
            ReservationFilterParams filterParams    //@ModelAttribute
    ) {
        List<ReservationResponse> reservations = reservationService.findAllByFilter(filterParams);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable
            Long id
    ) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
