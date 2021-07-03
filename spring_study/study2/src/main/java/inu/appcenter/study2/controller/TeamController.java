package inu.appcenter.study2.controller;

import inu.appcenter.study2.domain.Team;
import inu.appcenter.study2.model.team.TeamResponse;
import inu.appcenter.study2.model.team.TeamSaveRequest;
import inu.appcenter.study2.model.team.TeamUpdateRequest;
import inu.appcenter.study2.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/teams")
    public ResponseEntity saveTeam(@RequestBody TeamSaveRequest teamSaveRequest){
        teamService.saveTeam(teamSaveRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/teams/{teamId}")
    public ResponseEntity updateTeam(@PathVariable Long teamId,
                                     @RequestBody TeamUpdateRequest teamUpdateRequest){
        teamService.updateTeam(teamId,teamUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity deleteTeam(@PathVariable Long teamId){
        teamService.deleteTeam(teamId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //  팀 목록 조회
    @GetMapping("/teams")
    public List<TeamResponse> findAllTeam(){
        List<Team> teams = teamService.findAll();

        List<TeamResponse> teamResponseList = teams.stream().map(team -> new TeamResponse(team))
                .collect(Collectors.toList());

        return teamResponseList;
    }

    //  팀 1개 조회
    @GetMapping("/teams/{teamId}")
    public TeamResponse findById(@PathVariable Long teamId) {
        Team team = teamService.findById(teamId);

        return new TeamResponse(team);
    }
}
