package roomescape.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.config.AuthMember;
import roomescape.member.domain.Member;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.ReservationOutput;
import roomescape.reservation.service.dto.ReservationRequest;

import java.net.URI;

@RestController
@RequestMapping(ReservationApiController.BASE_URL)
@RequiredArgsConstructor
public class ReservationApiController {
    public static final String BASE_URL = "/reservations";
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationOutput> create(
            @RequestBody
            @Valid
            ReservationRequest requestDto,

            @AuthMember
            Member member
    ) {
        ReservationOutput reservationOutput = reservationService.save(requestDto.toInput(member.getId()));
        URI uri = URI.create("/reservations/" + member.getId());

        return ResponseEntity.created(uri).body(reservationOutput);
    }
}
