package org.mint.smallcloud.user.repository;

import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);
    List<Member> findByNicknameLike(String q);
    List<Member> findMembersByRole(Role role);
}
