package roomescape.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.BasicService;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.MemberErrorStatus;
import roomescape.member.controller.dto.MemberRequest;
import roomescape.member.controller.dto.MemberResponseDto;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements BasicService<Member, Long> {
    private final MemberRepository memberRepository;

    @Override
    public Member save(Member entity) {
        if (memberRepository.existsByEmail(entity.getEmail())) {
            throw new RestApiException(MemberErrorStatus.DUPLICATE_EMAIL);
        }
        return memberRepository.save(entity);
    }

    @Override
    public void delete(Member entity) {
        if (!memberRepository.existsById(entity.getId())) {
            throw new RestApiException(MemberErrorStatus.NOT_FOUND);
        }
        memberRepository.deleteById(entity.getId());
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RestApiException(MemberErrorStatus.NOT_FOUND));
    }

    public Member findByEmailAndPassword(String email, String password) {
        Member member = findByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new RestApiException(MemberErrorStatus.INVALID_PASSWORD);
        }
        return member;
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(MemberErrorStatus.NOT_FOUND));
    }

    public MemberResponseDto save(MemberRequest memberRequest) {
        Member member = Member.builder()
                .name(memberRequest.name())
                .email(memberRequest.email())
                .password(memberRequest.password())
                .build();
        return new MemberResponseDto(save(member));
    }
}
