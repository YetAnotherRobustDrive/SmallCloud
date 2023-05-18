package org.mint.smallcloud.board.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.domain.Answer;
import org.mint.smallcloud.board.dto.RequestDto;
import org.mint.smallcloud.board.repository.AnswerRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public boolean registerAnswer(RequestDto requestDto) {
        Answer answer = Answer.answer(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getBoard()
        );
        answerRepository.save(answer);
        return true;
    }

}
