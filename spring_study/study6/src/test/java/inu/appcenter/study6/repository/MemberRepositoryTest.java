package inu.appcenter.study6.repository;

import inu.appcenter.study6.domain.Member;
import inu.appcenter.study6.domain.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest //통합테스트
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) //내장 데이터베이스 즉 메모리에서 테스트
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 데이터베이스에서 테스트
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    //@Transactional 테스트 코드에 @Transactional은 테스트 코드 실행 후 롤백
    @Test
    @DisplayName("회원 저장")
    public void saveMember(){
        // given (어떤 환경, 상황에서)
        Member member = Member.createMember("황주환", 25);

        // when (이러한 행동을 했을 때)
        Member savedMember = memberRepository.save(member);

        //then (이러한 결과가 나올것이다라고 예측)
        assertNotNull(savedMember.getId());
    }

    @Test
    @DisplayName("회원 단일 조회")
    public void findMember(){
        //given
        Member member = Member.createMember("황주환", 25);
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        //then
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getName()).isEqualTo(savedMember.getName());
        assertThat(findMember.getAge()).isEqualTo(savedMember.getAge());
        assertThat(findMember.getStatus()).isEqualTo(savedMember.getStatus());
    }

    @Test
    @DisplayName("회원 목록 조회")
    public void findMembers(){
        // given
        Member member1 = Member.createMember("황주환", 25);
        Member member2 = Member.createMember("박재권", 20);
        Member member3 = Member.createMember("정윤아", 23);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        // when
        List<Member> result = memberRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(3);

    }

    @Test
    @DisplayName("삭제 회원 조회")
    public void findDeleteMember(){
        // given
        Member member1 = Member.createMember("황주환", 25);
        Member member2 = Member.createMember("박재권", 20);
        Member member3 = Member.createMember("정윤아", 23);
        member3.delete();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        Member findMember = memberRepository.findByStatus(MemberStatus.DELETE).get();

        //then
        assertThat(findMember.getName()).isEqualTo(member3.getName());
    }
}