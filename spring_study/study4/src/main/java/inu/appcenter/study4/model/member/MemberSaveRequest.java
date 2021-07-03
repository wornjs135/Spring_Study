package inu.appcenter.study4.model.member;

import lombok.Data;

@Data
public class MemberSaveRequest {

    private String email;

    private String password;

    private String name;
}
