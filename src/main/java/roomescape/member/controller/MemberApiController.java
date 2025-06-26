package roomescape.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.controller.dto.request.MemberCreateRequest;
import roomescape.member.controller.dto.response.MemberResponse;
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
    public ResponseEntity<MemberResponse> createMember(
            @RequestBody
            @Valid
            MemberCreateRequest memberCreateRequest
    ) {
        return ResponseEntity.ok().body(memberService.save(memberCreateRequest.toCommand()));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> responseDto = memberService.findAll()
                .stream()
                .map(MemberResponse::new)
                .toList();
        return ResponseEntity.ok(responseDto);
    }
}
