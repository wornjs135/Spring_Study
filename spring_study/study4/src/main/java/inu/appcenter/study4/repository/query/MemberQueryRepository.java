package inu.appcenter.study4.repository.query;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inu.appcenter.study4.domain.Member;
import inu.appcenter.study4.domain.QMember;
import inu.appcenter.study4.domain.QTodo;
import inu.appcenter.study4.domain.TodoStatus;
import inu.appcenter.study4.dto.MemberWithTodoCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //  스프링 빈으로 등록, 데이터베이스 오류나 JPa오류를 스프링의 오류로 전환해줘서 에러설명 깔끔하다.
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory; //  우리가 등록한 JPAQueryFactory

    public Member findMemberById(Long memberId){
        Member result = queryFactory
                .select(QMember.member).distinct()
                .from(QMember.member)
                .leftJoin(QMember.member.todoList, QTodo.todo).fetchJoin()
                .where(QMember.member.id.eq(memberId).and(QTodo.todo.status.eq(TodoStatus.NOTFINISH)))
                .fetchOne(); //결과가 1개값일때 사용  리스트일때는 fetch();

        return result;
    }

    public MemberWithTodoCount findMemberWithTodoCountsById(Long memberId){
        Tuple tuple = queryFactory.select(QMember.member, QTodo.todo.count())
                .from(QMember.member)
                .leftJoin(QMember.member.todoList, QTodo.todo)
                .groupBy(QMember.member)
                .where(QMember.member.id.eq(memberId))
                .fetchOne();
        MemberWithTodoCount memberWithTodoCount = new MemberWithTodoCount();
        memberWithTodoCount.setMember(tuple.get(QMember.member));
        memberWithTodoCount.setCount(tuple.get(QTodo.todo.count()));
        return memberWithTodoCount;
    }

    public List<Member> findMembersWithTodo(){
        List<Member> result = queryFactory
                .select(QMember.member).distinct()
                .from(QMember.member)
                .leftJoin(QMember.member.todoList, QTodo.todo).fetchJoin()
                .fetch();
        return result;
    }

}
