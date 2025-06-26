package roomescape.theme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ThemeErrorStatus;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.command.ThemeCreateCommand;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse findById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ThemeErrorStatus.NOT_FOUND));
        return ThemeResponse.from(theme);
    }

    public ThemeResponse save(ThemeCreateCommand createCommand) {
        Theme theme = save(createCommand.toEntity());

        return ThemeResponse.from(theme);
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
        if (reservationRepository.existsByThemeId(id)) {
            throw new RestApiException(ThemeErrorStatus.RESERVATION_EXIST);
        }
    }
}
