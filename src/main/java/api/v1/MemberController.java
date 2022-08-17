package api.v1;

import api.v1.dto.MultiResponseDto;
import api.v1.dto.SingleResponseDto;
import api.v1.member.mapper.MemberMapper;
import api.v1.member.entity.Member;
import api.v1.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/members")
public class MemberController {
    private final MemberService memberService;

    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }


    /**
     *단일 회원 조회
     */
    @GetMapping("/{member-id}")
    public ResponseEntity getMember(
            @PathVariable("member-id") long memberId) {
        Member member = memberService.findMember(memberId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.memberToMemberResponseDto(member))
                , HttpStatus.OK);
    }

    /**
     전체 회원 조회
     */
    @GetMapping
    public ResponseEntity getMembers(@RequestParam int page,
                                     @RequestParam int size) {
        Page<Member> pageMembers = memberService.findMembers(page - 1, size);
        List<Member> members = pageMembers.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.membersToMemberResponsesDto(members),
                        pageMembers),
                HttpStatus.OK);
    }
}
