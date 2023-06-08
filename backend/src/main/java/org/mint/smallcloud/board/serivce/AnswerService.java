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
import org.mint.smallcloud.notification.event.NoticeAllEventAfterCommit;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public boolean registerAnswer(RequestDto requestDto) {
        Question question = questionRepository
            .findById(requestDto.getQuestionId())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_QUESTION));
        Answer answer = Answer.answer(requestDto.getContent(), question);
        answerRepository.save(answer);

        if(question != null) {
            Member member = memberRepository.findByUsername(question.getWriter())
                    .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_MEMBER));
            if (member.canLogin())
                applicationEventPublisher.publishEvent(
                        NoticeAllEventAfterCommit
                                .builder()
                                .content(String.format("%s 문의의 답변이(가) 등록되었습니다.", question.getTitle()))
                                .build()
                );
        }
        return true;
    }
}
