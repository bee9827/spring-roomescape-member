package roomescape.common.exception.status;

import org.springframework.http.HttpStatus;

public enum ReservationErrorStatus implements ErrorStatus {
    NOT_FOUND(HttpStatus.NOT_FOUND,"RESERVATION_001","요청된 예약을 찾을 수 없습니다."),
    DUPLICATE(HttpStatus.CONFLICT, "RESERVATION_002","중복 예약은 불가능 합니다."),
    PAST_DATE_TIME(HttpStatus.CONFLICT,"RESERVATION_003","지나간 날짜와 시간에 대한 예약 생성은 불가능 합니다."),
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND,"RESERVATION_004","요청된 시간을 찾을 수 없어 예약이 불가능 합니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION_005", "요청된 테마를 찾을 수 없어 예약이 불가능 합니다.");

    private HttpStatus httpStatus;
    private String code;
    private String message;


    ReservationErrorStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
