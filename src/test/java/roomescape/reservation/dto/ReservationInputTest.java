package roomescape.reservation.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import roomescape.reservation.service.dto.command.ReservationCreateCommand;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationInputTest {

    @Test
    public void notNull() {
        ReservationCreateCommand reservationCreateCommand = new ReservationCreateCommand(null, null, null, null);
        Set<ConstraintViolation<ReservationCreateCommand>> violations = validate(reservationCreateCommand);

        violations.forEach(
                violation -> assertThat(violation).isInstanceOf(ConstraintViolation.class)
        );
    }

    private Set<ConstraintViolation<ReservationCreateCommand>> validate(ReservationCreateCommand reservationCreateCommand) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator.validate(reservationCreateCommand);
    }
}
