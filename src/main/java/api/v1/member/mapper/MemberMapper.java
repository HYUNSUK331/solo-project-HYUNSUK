package api.v1.member.mapper;

import api.v1.member.dto.MemberResponseDto;
import api.v1.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper (componentModel = "spring")
public interface MemberMapper {
    MemberResponseDto memberToMemberResponseDto(Member member);
    List<MemberResponseDto> membersToMemberResponsesDto(List<Member> members);
}
