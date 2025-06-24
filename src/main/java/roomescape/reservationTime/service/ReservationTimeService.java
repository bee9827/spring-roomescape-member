package roomescape.reservationTime.service;

import org.springframework.stereotype.Service;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public void deleteById(Long id) {
        reservationTimeRepository.deleteById(id);
    }

    public ReservationTime save(ReservationTime entity) {
        return reservationTimeRepository.save(entity);
    }

    public List<ReservationTime> findAvailable(Long themeId, LocalDate date) {
        return reservationTimeRepository.findAvailable(themeId, date);
    }

}
