package inu.appcenter.study6.service;

import inu.appcenter.study6.domain.Member;
import inu.appcenter.study6.model.MemberCreateRequest;
import inu.appcenter.study6.model.MemberUpdateRequest;
import inu.appcenter.study6.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public Member saveMember(MemberCreateRequest memberCreateRequest) {
        Member member = Member.createMember(memberCreateRequest.getName(), memberCreateRequest.getAge());

        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException());

        findMember.update(memberUpdateRequest.getName(), memberUpdateRequest.getAge());
        return findMember;
    }

    public boolean deleteMember(Long memeberId) {
        Member findMember = memberRepository.findById(memeberId)
                .orElseThrow(() -> new IllegalArgumentException());

        findMember.delete();

        return true;
    }
}
