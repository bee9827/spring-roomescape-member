package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.AvailableTimeResponse;
import roomescape.controller.dto.response.ReservationTimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.command.ReservationTimeCreateCommand;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse save(ReservationTimeCreateCommand createCommand) {
        return ReservationTimeResponse.from(save(createCommand.toEntity()));
    }

    public List<AvailableTimeResponse> findAvailable(Long themeId, LocalDate date) {
        return reservationTimeRepository.findAvailable(themeId, date)
                .stream()
                .map(AvailableTimeResponse::from)
                .toList();
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    private ReservationTime save(ReservationTime entity) {
        return reservationTimeRepository.save(entity);
    }

}
