package org.mint.smallcloud.label.repository;

import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    boolean existsByNameAndOwner(String name, Member owner);

    Label findByNameAndOwner(String labelName, Member owner);

}
