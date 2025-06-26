package roomescape.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.MemberErrorStatus;
import roomescape.common.exception.status.ReservationErrorStatus;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.policy.Policy;
import roomescape.reservation.controller.dto.request.ReservationSearchCriteria;
import roomescape.reservation.controller.dto.response.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.policy.DuplicateReservationPolicy;
import roomescape.reservation.domain.policy.PastDateTimeReservationPolicy;
import roomescape.reservation.service.dto.command.ReservationCreateCommand;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.Clock;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository jpaReservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final Clock clock;

    public ReservationResponse save(ReservationCreateCommand reservationCreateCommand) {
        Member member = memberRepository.findById(reservationCreateCommand.memberId())
                .orElseThrow(() -> new RestApiException(MemberErrorStatus.NOT_FOUND));
        ReservationTime time = reservationTimeRepository.findById(reservationCreateCommand.reservationTimeId())
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(reservationCreateCommand.themeId())
                .orElseThrow(() -> new RestApiException(ReservationErrorStatus.THEME_NOT_FOUND));

        Reservation createRequest = reservationCreateCommand.toEntity(member, theme, time);

        return ReservationResponse.from(save(createRequest));
    }

    public ReservationResponse findById(Long id) {
        Reservation reservation = jpaReservationRepository.findById(id);
        return ReservationResponse.from(reservation);
    }

    public List<ReservationResponse> findAll() {
        return jpaReservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        if (!jpaReservationRepository.existsById(id)) {
            throw new RestApiException(ReservationErrorStatus.NOT_FOUND);
        }

        jpaReservationRepository.deleteById(id);
    }

    public List<ReservationResponse> searchByCriteria(ReservationSearchCriteria reservationSearchCriteria) {
        return jpaReservationRepository.findByCriteria(reservationSearchCriteria)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private Reservation save(Reservation reservation) {
        List<Policy<Reservation>> reservationPolicies = List.of(
                new PastDateTimeReservationPolicy(clock),
                new DuplicateReservationPolicy(jpaReservationRepository)
        );

        reservation.validate(reservationPolicies);

        return jpaReservationRepository.save(reservation);
    }
}
