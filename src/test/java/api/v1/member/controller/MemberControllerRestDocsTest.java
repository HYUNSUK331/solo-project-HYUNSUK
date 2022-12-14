package api.v1.member.controller;


import api.v1.MemberController;
import api.v1.member.entity.Member;
import api.v1.member.mapper.MemberMapper;
import api.v1.member.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import api.v1.member.dto.MemberResponseDto;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MemberControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberMapper mapper;

    @Autowired
    private Gson gson;

    @Test
    public void getMemberTest() throws Exception {
        //given
        MemberResponseDto response = new MemberResponseDto(
                1L,
                "texy0@gmail.com",
                "???",
                28,
                "????????????");

        //when
        given(memberService.findMember(Mockito.anyLong())).willReturn(new Member());
        given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        ResultActions actions = mockMvc.perform(
                get("/v1/members/{member-id}", 1)
        );

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value(response.getUsername()))
                .andDo(document(
                        "get-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("member-id").description("?????? ?????????")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("data.username").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.age").type(JsonFieldType.NUMBER).description("??????"),
                                        fieldWithPath("data.gender").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.companyName").type(JsonFieldType.STRING).description("?????????")
                                )
                        )
                ));

    }

    @Test
    public void getMembersTest() throws Exception {
        //given
        List<MemberResponseDto> responses = List.of(
                new MemberResponseDto(1L,
                        "test0@gmail.com",
                        "???",
                        28,
                        "????????????"),
                new MemberResponseDto(2L,
                        "test1@gmail.com",
                        "?????????",
                        20,
                        "??????????????????"),
                new MemberResponseDto(3L,
                        "test2@gmail.com",
                        "?????????",
                        23,
                        "?????????")
        );

        Page<Member> page = new PageImpl(responses);
        //when
        given(memberService.findMembers(Mockito.anyInt(),Mockito.anyInt())).willReturn(page);
        given(mapper.membersToMemberResponsesDto(Mockito.anyList())).willReturn(responses);

        ResultActions ac = mockMvc.perform(
                get("/v1/members")
                        .param("page", "1")
                        .param("size", "10")
        );

        //then
        ac.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].username").value(responses.get(0).getUsername()))
                .andDo(document(
                        "get-members",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("Page ??????"),
                                        parameterWithName("size").description("Page Size")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data[].email").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("data[].username").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data[].age").type(JsonFieldType.NUMBER).description("??????"),
                                        fieldWithPath("data[].gender").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data[].companyName").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("????????? ??????").optional(),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("????????? ??????").optional(),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("????????? ?????????").optional(),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("?????? ??? ???").optional(),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("?????? ????????? ???").optional()
                                )
                        )
                ));

    }
}
