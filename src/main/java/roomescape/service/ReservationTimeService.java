package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationTimeErrorStatus;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.command.ReservationTimeCreateCommand;
import roomescape.service.dto.result.AvailableReservationTimeResult;
import roomescape.service.dto.result.ReservationTimeResult;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationTimeService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeResult save(ReservationTimeCreateCommand createCommand) {
        return ReservationTimeResult.from(save(createCommand.toEntity()));
    }

    public List<AvailableReservationTimeResult> findAvailable(Long themeId, LocalDate date) {
        return reservationTimeRepository.findAvailable(themeId, date);
    }

    public List<ReservationTimeResult> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    public void deleteById(Long id) {
        validateReservationExists(id);

        reservationTimeRepository.delete(findById(id));
    }

    private ReservationTime findById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ReservationTimeErrorStatus.NOT_FOUND));
    }

    private void validateReservationExists(Long id) {
        if (reservationRepository.existsByReservationTimeId(id))
            throw new RestApiException(ReservationTimeErrorStatus.RESERVATION_EXIST);
    }

    private ReservationTime save(ReservationTime entity) {
        validateDuplicate(entity);
        return reservationTimeRepository.save(entity);
    }

    private void validateDuplicate(ReservationTime reservationTime) {
        if(reservationTimeRepository.existsByStartAt(reservationTime.getStartAt())){
            throw new RestApiException(ReservationTimeErrorStatus.DUPLICATE);
        };
    }

}
