package inu.appcenter.study4.service;

import inu.appcenter.study4.config.security.JwtTokenProvider;
import inu.appcenter.study4.domain.Member;
import inu.appcenter.study4.model.member.MemberLoginRequest;
import inu.appcenter.study4.model.member.MemberSaveRequest;
import inu.appcenter.study4.repository.MemberRepository;
import inu.appcenter.study4.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberQueryRepository memberQueryRepository;

    // 어드민 계정 생성. 서버실행할때 자동으로 됨.
    @PostConstruct
    public void saveAdminMember(){
        Member adminMember = Member.creteAdminMember("admin", passwordEncoder.encode("admin"), "박재권");
        memberRepository.save(adminMember);
    }

    @Transactional
    public void saveMember(MemberSaveRequest memberSaveRequest) {
        Member member = Member.createMember(memberSaveRequest.getEmail(),
                passwordEncoder.encode(memberSaveRequest.getPassword()),
                memberSaveRequest.getName());

        memberRepository.save(member);
    }

    public Member loginMember(MemberLoginRequest memberLoginRequest) {

        /**
        * 2. 이메일로 회원을 조회해 와서 비밀번호가 일치하는 지 PasswordEncoder를 사용해서 확인
         * 로그인 요청 비밀번호와 DB의 암호화된 비밀번호가 일치하는지 확인.
        * */
        Member findMember = memberRepository.findByEmail(memberLoginRequest.getEmail())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 이메일입니다."));
        if(!passwordEncoder.matches(memberLoginRequest.getPassword(), findMember.getPassword())){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        return findMember;
    }

    public List<Member> findMembers() {
        List<Member> membersWithTodo = memberQueryRepository.findMembersWithTodo();
        return membersWithTodo;
    }
}
