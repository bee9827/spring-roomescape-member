package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.ReservationResponseDto;
import roomescape.reservation.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(ReservationApiController.BASE_URL)
public class ReservationApiController {
    public static final String BASE_URL = "/reservations";

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getReservations() {
        List<ReservationResponseDto> reservations = reservationService.findAll()
                .stream()
                .map(ReservationResponseDto::new)
                .toList();

        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservation(
            @PathVariable
            Long id
    ) {
        ReservationResponseDto reservation = new ReservationResponseDto(reservationService.findById(id));
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable
            Long id
    ) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody
            @Valid
            ReservationRequestDto reservationRequestDto
    ) {
        ReservationResponseDto reservationResponse = new ReservationResponseDto(reservationService.save(reservationRequestDto));
        URI uri = URI.create(BASE_URL + "/" + reservationResponse.id());


        return ResponseEntity.created(uri).body(reservationResponse);
    }
}
