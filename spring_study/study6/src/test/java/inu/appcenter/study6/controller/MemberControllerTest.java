package inu.appcenter.study6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import inu.appcenter.study6.domain.Member;
import inu.appcenter.study6.domain.MemberStatus;
import inu.appcenter.study6.model.MemberCreateRequest;
import inu.appcenter.study6.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs // rest docs 설정
@ExtendWith(RestDocumentationExtension.class) // rest docs 확장
class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext wac, RestDocumentationContextProvider restDocumentationContextProvider){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .apply(documentationConfiguration(restDocumentationContextProvider)// mockMvc Rest Docs 설정
                .operationPreprocessors()
                .withRequestDefaults(prettyPrint()) // 예쁘게 포맷팅
                .withResponseDefaults(prettyPrint())) // 예쁘게 포맷팅
                .build();

    }

    @Test
    @DisplayName("회원 생성 api")
    void saveMember() throws Exception{

        MemberCreateRequest memberCreateRequest = new MemberCreateRequest();
        memberCreateRequest.setAge(25);
        memberCreateRequest.setName("박재권");

        String body = objectMapper.writeValueAsString(memberCreateRequest);

        Member member = Member.createMember(1L, "박재권", 25);
        given(memberService.saveMember(any()))
                .willReturn(member);

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("박재권"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andDo(document("member/create", requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                        fieldWithPath("age").type(JsonFieldType.NUMBER).description("회원 나이")
                ),
                responseFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                        fieldWithPath("age").type(JsonFieldType.NUMBER).description("회원 나이"),
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 ID"),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("회원 상태")
                ))
                );

        then(memberService).should(times(1)).saveMember(any());
    }

}