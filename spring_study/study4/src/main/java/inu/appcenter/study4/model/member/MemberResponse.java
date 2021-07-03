package inu.appcenter.study4.model.member;

import inu.appcenter.study4.domain.Member;
import inu.appcenter.study4.domain.MemberStatus;
import inu.appcenter.study4.model.todo.TodoResponse;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberResponse {
    private Long memberId;

    private String email;

    private String name;

    private MemberStatus status;

    List<TodoResponse> todoList;

    public static MemberResponse from(Member member){
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.email = memberResponse.getEmail();
        memberResponse.memberId = member.getId();
        memberResponse.name = member.getName();
        memberResponse.status = member.getStatus();
        memberResponse.todoList = member.getTodoList().stream()
                .map(todo -> TodoResponse.from(todo)).collect(Collectors.toList());
        return memberResponse;
    }
}
