package org.mint.smallcloud.board.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.board.domain.Question;
import org.mint.smallcloud.board.repository.QuestionRepository;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardThrowerService {
    private final QuestionRepository questionRepository;

    public Question findById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_INQUIRY));
    }
}
