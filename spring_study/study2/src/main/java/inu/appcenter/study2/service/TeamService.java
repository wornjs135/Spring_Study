package inu.appcenter.study2.service;

import inu.appcenter.study2.domain.Team;
import inu.appcenter.study2.exception.TeamException;
import inu.appcenter.study2.model.team.TeamSaveRequest;
import inu.appcenter.study2.model.team.TeamUpdateRequest;
import inu.appcenter.study2.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public void saveTeam(TeamSaveRequest teamSaveRequest){

        //  중복체크
        Optional<Team> result = teamRepository.findByName(teamSaveRequest.getName());
        if(result.isPresent()) {
            throw new TeamException("팀이름이 중복되었습니다.");
        }

        //  팀 저장
        Team team = Team.createTeam(teamSaveRequest.getName());
        teamRepository.save(team);
    }

    @Transactional
    public void updateTeam(Long teamId, TeamUpdateRequest teamUpdateRequest) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException("존재하지 않는 팀id입니다."));

        //  중복체크
        Optional<Team> result = teamRepository.findByName(teamUpdateRequest.getName());
        if(result.isPresent()) {
            throw new TeamException("팀이름이 중복되었습니다.");
        }

        team.changeName(teamUpdateRequest.getName());
    }


    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException("존재하지 않는 팀id입니다."));

        teamRepository.delete(team);
    }

    public List<Team> findAll() {
        List<Team> teams = teamRepository.findAll();

        return teams;
    }

    public Team findById(Long teamId){
        Team team = teamRepository.findWithMemberById(teamId)
                .orElseThrow(() -> new TeamException("존재하지 않는 팀id입니다"));

        return team;
    }
}
