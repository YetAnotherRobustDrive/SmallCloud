package org.mint.smallcloud.group.repository;

import org.mint.smallcloud.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);

    boolean existsByName(String name);

    List<Group> findAllByParentGroup(Group group);


    List<Group> findByNameLike(String s);
}
