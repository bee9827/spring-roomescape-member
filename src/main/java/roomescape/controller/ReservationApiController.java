package roomescape.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.resolver.AuthMember;
import roomescape.controller.dto.request.ReservationCreateRequestForMember;
import roomescape.controller.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;

@RestController
@RequestMapping(ReservationApiController.BASE_URL)
@RequiredArgsConstructor
public class ReservationApiController {
    public static final String BASE_URL = "/reservations";
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @RequestBody
            @Valid
            ReservationCreateRequestForMember requestDto,

            @AuthMember
            Long authMemberId
    ) {
        ReservationResponse reservationResponse = ReservationResponse.from(reservationService.save(requestDto.toCommand(authMemberId)));
        URI uri = URI.create("/reservations/" + authMemberId);

        return ResponseEntity.created(uri).body(reservationResponse);
    }
}
