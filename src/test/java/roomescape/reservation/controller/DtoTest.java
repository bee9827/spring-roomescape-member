package roomescape.reservation.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.ReservationResponseDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class DtoTest {

    @Test
    public void notEmpty() {
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(null,null,null,null);
        Set<ConstraintViolation<ReservationRequestDto>> violations = validate(reservationRequestDto);

        assertThat(violations).isEmpty();
    }

    private Set<ConstraintViolation<ReservationRequestDto>> validate(ReservationRequestDto reservationRequestDto) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator.validate(reservationRequestDto);
    }
}
