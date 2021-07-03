package inu.appcenter.study6.service;

import inu.appcenter.study6.domain.Member;
import inu.appcenter.study6.domain.MemberStatus;
import inu.appcenter.study6.model.MemberCreateRequest;
import inu.appcenter.study6.model.MemberUpdateRequest;
import inu.appcenter.study6.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks // memberRepository를 memberService에 주입해준다.
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository; // 가짜 객체

    @Test
    @DisplayName("회원 생성")
    public void saveMember(){
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest();
        memberCreateRequest.setName("황주환");
        memberCreateRequest.setAge(25);

        Member member = Member.createMember(1L, "황주환", 25);

        given(memberRepository.save(any()))
                .willReturn(member);

        // when
        Member savedMember = memberService.saveMember(memberCreateRequest);

        // then
        assertThat(savedMember.getId()).isEqualTo(1L);
        assertThat(savedMember.getName()).isEqualTo(memberCreateRequest.getName());
        assertThat(savedMember.getAge()).isEqualTo(memberCreateRequest.getAge());
        assertThat(savedMember.getStatus()).isEqualTo(MemberStatus.ACTIVE);

        then(memberRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("회원 수정")
    public void updateMember(){

        //given
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
        memberUpdateRequest.setAge(20);
        memberUpdateRequest.setName("박재권");

        Member member1 = Member.createMember(1L, "황주환", 25);

        given(memberRepository.findById(any()))
                .willReturn(Optional.of(member1));

        // when
        Member updateMember = memberService.updateMember(1L, memberUpdateRequest);

        // then
        assertThat(updateMember.getId()).isEqualTo(1L);
        assertThat(updateMember.getName()).isEqualTo(memberUpdateRequest.getName());
        assertThat(updateMember.getAge()).isEqualTo(memberUpdateRequest.getAge());

        then(memberRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("회원 삭제")
    public void deleteMember(){

        Member member = Member.createMember(1L, "박재권", 25);

        given(memberRepository.findById(any()))
                .willReturn(Optional.of(member));

        boolean result = memberService.deleteMember(1L);

        assertThat(result).isEqualTo(true);

        then(memberRepository).should(times(1)).findById(any());
    }
}