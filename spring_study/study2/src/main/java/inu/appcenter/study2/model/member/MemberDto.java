package inu.appcenter.study2.model.member;

import inu.appcenter.study2.domain.Member;
import inu.appcenter.study2.model.team.TeamResponse;
import lombok.Data;

@Data
public class MemberDto {

    private Long memberId;

    private String email;

    private String name;

    private int age;

    public MemberDto(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.age = member.getAge();
    }

}
