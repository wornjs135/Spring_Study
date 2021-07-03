package inu.appcenter.study6.model;

import inu.appcenter.study6.domain.Member;
import inu.appcenter.study6.domain.MemberStatus;
import lombok.Data;

@Data
public class MemberResponse {

    private Long id;

    private String name;

    private Integer age;

    private MemberStatus status;

    public static MemberResponse from(Member member){
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.age = member.getAge();
        memberResponse.name = member.getName();
        memberResponse.id = member.getId();
        memberResponse.status = member.getStatus();
        return memberResponse;
    }
}
