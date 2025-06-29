package roomescape.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.resolver.AuthMember;
import roomescape.reservation.controller.dto.request.ReservationCreateRequestForMember;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.result.ReservationResult;

import java.net.URI;

@RestController
@RequestMapping(ReservationApiController.BASE_URL)
@RequiredArgsConstructor
public class ReservationApiController {
    public static final String BASE_URL = "/reservations";
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResult> create(
            @RequestBody
            @Valid
            ReservationCreateRequestForMember requestDto,

            @AuthMember
            Long authMemberId
    ) {
        ReservationResult reservationResponse = reservationService.save(requestDto.toCommand(authMemberId));
        URI uri = URI.create("/reservations/" + authMemberId);

        return ResponseEntity.created(uri).body(reservationResponse);
    }
}
