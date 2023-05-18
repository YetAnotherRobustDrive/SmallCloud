package org.mint.smallcloud.board.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.domain.Question;
import org.mint.smallcloud.board.dto.QuestionDto;
import org.mint.smallcloud.board.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final BoardThrowerService questionThrowerService;
    private final QuestionRepository questionRepository;

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public QuestionDto findById(Long questionId) throws Exception {

        Question question = questionThrowerService.findById(questionId);
        return QuestionDto.builder()
                .title(question.getTitle())
                .content(question.getContent())
                .contact(question.getContact())
                .writer(question.getWriter())
                .build();
    }
    public void save(QuestionDto questionDto) {
        Question board = Question.question(
                questionDto.getTitle(),
                questionDto.getContent(),
                questionDto.getContact(),
                questionDto.getWriter());
        questionRepository.save(board);
    }
}
