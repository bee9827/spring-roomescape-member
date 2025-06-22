package roomescape.common.exception.status;

import org.springframework.http.HttpStatus;

public enum ReservationTimeErrorStatus implements ErrorStatus {
    NOT_FOUND(HttpStatus.NOT_FOUND,"TIME_001","요청된 시간을 찾을 수 없습니다."),
    RESERVATION_EXIST(HttpStatus.CONFLICT,"TIME_002","해당 시간 예약이 존재 하여 삭제할 수 없습니다."),
    ;


    private HttpStatus httpStatus;
    private String code;
    private String message;

    ReservationTimeErrorStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
