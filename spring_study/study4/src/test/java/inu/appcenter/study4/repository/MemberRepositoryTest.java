package inu.appcenter.study4.repository;

import inu.appcenter.study4.domain.Member;
import inu.appcenter.study4.domain.Todo;
import inu.appcenter.study4.dto.MemberWithTodoCount;
import inu.appcenter.study4.repository.query.MemberQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@Transactional //   모듣 테스트가 끝나고 rollback
//@DataJpaTest    //  데이터 액세스 계층의 빈들을 가져옴. EntityManager, DataSource, @Repository 빈들 가져옵니다.
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //H2 내장 디비로 테스트
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MemberQueryRepository memberQueryRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("회원 저장 테스트")
    void test(){
        Member member = Member.createMember("rjeowornjs@naver.com", "1111", "박재권");

        Member savedMember = memberRepository.save(member);

        assertNotNull(savedMember.getId()); //아마 널이 아닐거야
    }

    //  회원 - 글 글의 상태가 DELETE가 아닌걸 조회
    @Test
    @DisplayName("회원 - todo 같이 조회")
    void findMemberById(){
        Member member = Member.createMember("rjeowornjs@naver.com", "1111", "박재권");
        memberRepository.save(member);

        Todo todo1 = Todo.createTodo("앱센스터디", member);
        Todo todo2 = Todo.createTodo("db 스터디", member);
        Todo todo3 = Todo.createTodo("서버 스터디", member);
        todo3.changeStatus();

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        em.flush();//   영속성 컨텍스트에 있는 쿼리 날라감
        em.clear();//   영속성 컨텍스트 초기화

        Member findMember = memberQueryRepository.findMemberById(1L);

        assertThat(findMember.getTodoList().size()).isEqualTo(2);
    }

    //  댓글 수 찾아오는 방법
    //  회원 조회하면서 todo의 갯수 조회
    @Test
    @DisplayName("회원 조회 - todo 갯수")
    void findMemberWithTodoCountsById(){
        Member member = Member.createMember("rjeowornjs@naver.com", "1111", "박재권");
        memberRepository.save(member);

        Todo todo1 = Todo.createTodo("앱센스터디", member);
        Todo todo2 = Todo.createTodo("db 스터디", member);
        Todo todo3 = Todo.createTodo("서버 스터디", member);
        todo3.changeStatus();

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        em.flush();//   영속성 컨텍스트에 있는 쿼리 날라감
        em.clear();//   영속성 컨텍스트 초기화

        MemberWithTodoCount result = memberQueryRepository.findMemberWithTodoCountsById(1L);

        assertThat(result.getCount()).isEqualTo(3);

    }

}