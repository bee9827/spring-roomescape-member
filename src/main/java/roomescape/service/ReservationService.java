package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.controller.dto.request.ReservationSearchCriteria;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.command.ReservationCreateCommand;
import roomescape.service.dto.result.ReservationResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

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

    public List<ReservationResult> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResult::from)
                .toList();
    }

    public void deleteById(Long id) {
        Reservation reservation = findById(id);
        reservationRepository.delete(reservation);
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
        validate(reservation);

        return reservationRepository.save(reservation);
    }


    private Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.NOT_FOUND));
    }

    public void validate(Reservation reservation) {
        validateDuplicate(reservation);
    }

    private void validateDuplicate(Reservation reservation) {
        if (reservationRepository.isDuplicated(reservation.getTheme(), reservation.getDate(), reservation.getReservationTime()))
            throw new RestApiException(ReservationErrorStatus.DUPLICATE);
    }
}
