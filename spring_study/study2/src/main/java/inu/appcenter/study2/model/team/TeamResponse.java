package inu.appcenter.study2.model.team;

import inu.appcenter.study2.domain.Member;
import inu.appcenter.study2.domain.Team;
import inu.appcenter.study2.model.member.MemberDto;
import inu.appcenter.study2.model.member.MemberResponse;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeamResponse {

    private Long teamId;

    private String name;

    private List<MemberDto> members;

    public TeamResponse(Team team) {
        this.teamId = team.getId();
        this.name = team.getName();
        this.members = team.getMemberList().stream()
                .map(member -> new MemberDto(member))
                .collect(Collectors.toList());
    }
}
