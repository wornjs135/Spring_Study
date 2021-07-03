package inu.appcenter.study2.service;

import inu.appcenter.study2.domain.Member;
import inu.appcenter.study2.domain.Team;
import inu.appcenter.study2.exception.MemberException;
import inu.appcenter.study2.exception.TeamException;
import inu.appcenter.study2.model.member.MemberSaveRequest;
import inu.appcenter.study2.repository.MemberRepository;
import inu.appcenter.study2.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void save(Long teamId, MemberSaveRequest memberSaveRequest) {
        Team findTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException("존재하지 않는 팀id입니다."));

        Optional<Member> result = memberRepository.findByEmail(memberSaveRequest.getEmail());
        if(result.isPresent()) {
            throw new MemberException("이름이 중복되었습니다.");
        }

        Member member = Member.createMember(memberSaveRequest.getEmail(), memberSaveRequest.getName(),
                memberSaveRequest.getAge(), findTeam);

        memberRepository.save(member);
    }

    public Member findById(Long memberId) {
        Member member = memberRepository.findWithTeamById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원id입니다."));
        return member;
    }

    public List<Member> findAll() {
        return memberRepository.findWithTeamAll();
    }
}
