package inu.appcenter.study6.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    /**
     * 1. 실패하는 테스트를 작성한다.
     * 2. 테스트실행 -> 테스트가 깨진다.
     * 3. 테스트의 깨진 부분을 보완
     * 4. 다시 테스트 실행 -> 테스트 성공
     */

    @BeforeEach
    public void init(){
        Member member = Member.createMember("박재권", 20);

    }

    @Test
    @DisplayName("회원 생성")
    public void createMember(){

        // given

        // when
        Member member = Member.createMember("박재권", 20);


        // then
        assertThat(member.getName()).isEqualTo("박재권");
        assertThat(member.getAge()).isEqualTo(20);
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("회원 수정")
    public void updateMember(){
        // given
        Member member = Member.createMember("박재권", 20);

        // when
        member.update("황주환", 25);

        //then
        assertThat(member.getName()).isEqualTo("황주환");
        assertThat(member.getAge()).isEqualTo(25);
    }

    @Test
    @DisplayName("회원 삭제")
    public void deleteMember(){
        // given
        Member member = Member.createMember("박재권", 20);

        // when
        member.delete();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DELETE);
    }
}