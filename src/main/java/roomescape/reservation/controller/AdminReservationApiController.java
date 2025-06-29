package roomescape.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.controller.dto.request.ReservationCreateRequestForAdmin;
import roomescape.reservation.controller.dto.request.ReservationSearchCriteria;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.result.ReservationResult;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(AdminReservationApiController.BASE_URL)
@RequiredArgsConstructor
public class AdminReservationApiController {
    public static final String BASE_URL = "admin/reservations";

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResult> createReservation(
            @RequestBody
            @Valid
            ReservationCreateRequestForAdmin request
    ) {
        ReservationResult savedReservation = reservationService.save(request.toCommand());
        URI uri = URI.create(BASE_URL + "/" + savedReservation.id());

        return ResponseEntity.created(uri).body(savedReservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResult>> filterReservations(
            ReservationSearchCriteria reservationSearchCriteria    //@ModelAttribute
    ) {
        List<ReservationResult> reservations = reservationService.searchByCriteria(reservationSearchCriteria);
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
