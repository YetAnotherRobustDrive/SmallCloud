package org.mint.smallcloud.board.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.domain.Answer;
import org.mint.smallcloud.board.domain.Question;
import org.mint.smallcloud.board.dto.RequestDto;
import org.mint.smallcloud.board.repository.AnswerRepository;
import org.mint.smallcloud.board.repository.QuestionRepository;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public boolean registerAnswer(RequestDto requestDto) {
        Question question = questionRepository
            .findById(requestDto.getQuestionId())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_QUESTION));
        Answer answer = Answer.answer(requestDto.getTitle(), requestDto.getContent(), question);
        answerRepository.save(answer);
        return true;
    }
}
