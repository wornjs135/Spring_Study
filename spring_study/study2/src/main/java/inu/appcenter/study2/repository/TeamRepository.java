package inu.appcenter.study2.repository;

import inu.appcenter.study2.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);

    @Query("select distinct t From Team t join fetch t.memberList where t.id =:teamId")
    Optional<Team> findWithMemberById(@Param("teamId") Long teamId);
}
