package roomescape.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.controller.dto.request.ReservationSearchCriteria;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.command.ReservationCreateCommand;
import roomescape.reservation.service.dto.result.ReservationResult;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final ReservationValidator reservationValidator;

    public ReservationResult save(ReservationCreateCommand reservationCreateCommand) {
        Member member = memberRepository.findById(reservationCreateCommand.memberId())
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.MEMBER_NOT_FOUND));
        ReservationTime time = reservationTimeRepository.findById(reservationCreateCommand.reservationTimeId())
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(reservationCreateCommand.themeId())
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.THEME_NOT_FOUND));

        Reservation createRequest = reservationCreateCommand.toEntity(member, theme, time);

        return ReservationResult.from(save(createRequest));
    }

    public ReservationResult findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.NOT_FOUND));
        return ReservationResult.from(reservation);
    }

    public List<ReservationResult> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResult::from)
                .toList();
    }

    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RestApiException(ReservationErrorStatus.NOT_FOUND);
        }

        reservationRepository.deleteById(id);
    }

    public List<ReservationResult> searchByCriteria(ReservationSearchCriteria reservationSearchCriteria) {
        return filter(reservationSearchCriteria)
                .stream()
                .map(ReservationResult::from)
                .toList();
    }

    private List<Reservation> filter(ReservationSearchCriteria filter) {
        return reservationRepository.filter(
                filter.themeId(),
                filter.memberId(),
                filter.dateFrom(),
                filter.dateTo()
        );
    }

    private Reservation save(Reservation reservation) {
        reservationValidator.validate(reservation);

        return reservationRepository.save(reservation);
    }
}
