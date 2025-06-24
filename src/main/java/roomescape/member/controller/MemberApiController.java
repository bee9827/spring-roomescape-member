package roomescape.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.controller.dto.MemberRequest;
import roomescape.member.controller.dto.MemberResponseDto;
import roomescape.member.service.MemberService;

import java.util.List;

import static roomescape.member.controller.MemberApiController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class MemberApiController {
    public static final String BASE_URL = "/members";

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(
            @RequestBody
            @Valid
            MemberRequest memberRequest
    ) {
        return ResponseEntity.ok().body(memberService.save(memberRequest));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        List<MemberResponseDto> responseDto = memberService.findAll()
                .stream()
                .map(MemberResponseDto::new)
                .toList();
        return ResponseEntity.ok(responseDto);
    }
}
