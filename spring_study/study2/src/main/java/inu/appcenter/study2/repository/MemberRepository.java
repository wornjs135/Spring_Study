package inu.appcenter.study2.repository;

import inu.appcenter.study2.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("select m from Member m join fetch m.team where m.id =:memberId")
    Optional<Member> findWithTeamById(@Param("memberId") Long memberId);

    @Query("select m From Member m join fetch m.team")
    List<Member> findWithTeamAll();
}
