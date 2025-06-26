package roomescape.reservation.controller.dto.reqeust;

import org.springframework.web.bind.annotation.RequestParam;

public record ReservationFilterParams(
        @RequestParam(required = false) Long memberId,
        @RequestParam(required = false) Long themeId,
        @RequestParam(required = false) String dateFrom,
        @RequestParam(required = false) String dateTo
) {
}
