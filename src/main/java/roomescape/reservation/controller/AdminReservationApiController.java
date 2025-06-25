package roomescape.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.ReservationInput;
import roomescape.reservation.service.dto.ReservationOutput;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(AdminReservationApiController.BASE_URL)
@RequiredArgsConstructor
public class AdminReservationApiController {
    public static final String BASE_URL = "admin/reservations";

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationOutput> createReservation(
            @RequestBody
            @Valid
            ReservationInput reservationInput
    ) {
        ReservationOutput reservationResponse = reservationService.save(reservationInput);
        URI uri = URI.create(BASE_URL + "/" + reservationResponse.id());

        return ResponseEntity.created(uri).body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationOutput>> filterReservations(
            ReservationFilterParams filterParams    //@ModelAttribute
    ) {
        List<ReservationOutput> reservations = reservationService.findAllByFilter(filterParams);
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
