package org.mint.smallcloud.label.repository;

import org.mint.smallcloud.label.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository {
    boolean existsByLabelName(String labelName);

    //Label findByName(String LabelName);

    // like 붙여서 할건지 고민해야함
    List<Label> findByName(String labelName);
}
