package org.mint.smallcloud.board.serivce;

import org.mint.smallcloud.board.domain.Question;
import org.mint.smallcloud.board.dto.QuestionDto;
import org.mint.smallcloud.board.mapper.QuestionMapper;
import org.mint.smallcloud.board.repository.QuestionRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(QuestionService.class);
    private final BoardThrowerService boardThrowerService;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public QuestionService(BoardThrowerService boardThrowerService, QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.boardThrowerService = boardThrowerService;
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    public List<QuestionDto> findAll() {
        List<Question> question = questionRepository.findAll();
        return question.stream().map(questionMapper::toQuestionDto).collect(Collectors.toList());
    }

    public QuestionDto findById(Long questionId) throws Exception {

        Question question = boardThrowerService.findById(questionId);
        return QuestionDto.builder()
            .id(question.getId())
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

    public List<QuestionDto> findQuestioned() {
        List<Question> question = questionRepository.findByAnswer(null);
        return question.stream().map(questionMapper::toQuestionDto).collect(Collectors.toList());
    }

    public List<QuestionDto> findMyQuestions(String writer) {
        List<Question> question = questionRepository.findByWriter(writer);
        return question.stream().map(questionMapper::toQuestionDto).collect(Collectors.toList());
    }
}
