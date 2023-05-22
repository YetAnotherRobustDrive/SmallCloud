package org.mint.smallcloud.board.repository;

import org.mint.smallcloud.board.domain.Answer;
import org.mint.smallcloud.board.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByWriter(String writer);
    List<Question> findByAnswer(Answer answer);
}
