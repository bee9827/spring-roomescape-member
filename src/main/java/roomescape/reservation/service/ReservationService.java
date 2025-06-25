package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.MemberErrorStatus;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.controller.ReservationFilterParams;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationInput;
import roomescape.reservation.service.dto.ReservationOutput;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;


    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationOutput save(ReservationInput reservationInput) {
        Member member = memberRepository.findById(reservationInput.memberId())
                .orElseThrow(() -> new RestApiException(MemberErrorStatus.NOT_FOUND));
        ReservationTime time = reservationTimeRepository.findById(reservationInput.reservationTimeId())
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(reservationInput.themeId())
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.THEME_NOT_FOUND));

        Reservation requestReservation = reservationInput.toEntity(member, theme, time);

        return ReservationOutput.from(save(requestReservation));
    }

    public Reservation save(Reservation reservation) {
        validateDuplicate(reservation);

        return reservationRepository.save(reservation);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.NOT_FOUND));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll()
                .stream()
                .toList();
    }

    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RestApiException(ReservationErrorStatus.NOT_FOUND);
        }

        reservationRepository.deleteById(id);
    }

    public List<ReservationOutput> findAllByFilter(ReservationFilterParams filterParams) {
        Map<String, Object> filterParamsWithoutNull = new HashMap<>();

        try {
            Field[] fields = filterParams.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(field) == null) continue;
                filterParamsWithoutNull.put(field.getName(), field.get(field));
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("변수명 파싱 오류");
        }

        return reservationRepository.findAllByFilter(filterParamsWithoutNull)
                .stream()
                .map(ReservationOutput::from)
                .toList();
    }

    private void validateDuplicate(Reservation reservation) {
        if (reservationRepository.existsByDateAndThemeAndReservationTime(
                reservation.getDate(),
                reservation.getTheme(),
                reservation.getReservationTime())
        ) {
            throw new RestApiException(ReservationErrorStatus.DUPLICATE);
        }
    }
}
