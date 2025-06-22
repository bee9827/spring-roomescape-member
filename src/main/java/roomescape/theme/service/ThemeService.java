package roomescape.theme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ThemeErrorStatus;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.controller.dto.ThemeRequestDto;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme findById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ThemeErrorStatus.NOT_FOUND));
    }

    public Theme save(ThemeRequestDto themeRequestDto) {
        return save(themeRequestDto.toEntity());
    }

    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteById(Long id) {
        validateThemeExists(id);
        validateReservationExists(id);
        themeRepository.deleteById(id);
    }

    private void validateThemeExists(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new RestApiException(ThemeErrorStatus.NOT_FOUND);
        }
    }

    private void validateReservationExists(Long id) {
        if (reservationRepository.existsReservationByThemeId(id)) {
            throw new RestApiException(ThemeErrorStatus.RESERVATION_EXIST);
        }
    }
}
