package roomescape.reservation.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationRequestDtoTest {

    @Test
    public void notNull() {
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(null, null, null, null);
        Set<ConstraintViolation<ReservationRequestDto>> violations = validate(reservationRequestDto);

        violations.forEach(
                violation -> assertThat(violation).isInstanceOf(ConstraintViolation.class)
        );
    }

    private Set<ConstraintViolation<ReservationRequestDto>> validate(ReservationRequestDto reservationRequestDto) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator.validate(reservationRequestDto);
    }
}
