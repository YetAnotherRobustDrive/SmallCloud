package org.mint.smallcloud.board.serivce;

import org.mint.smallcloud.board.domain.Answer;
import org.mint.smallcloud.board.domain.Question;
import org.mint.smallcloud.board.dto.AnswerDto;
import org.mint.smallcloud.board.dto.RequestDto;
import org.mint.smallcloud.board.repository.AnswerRepository;
import org.mint.smallcloud.board.repository.QuestionRepository;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.notification.event.NoticeEventAfterCommit;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AnswerService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AnswerService.class);
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository, MemberRepository memberRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public boolean registerAnswer(RequestDto requestDto) {
        Question question = questionRepository
            .findById(requestDto.getQuestionId())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_QUESTION));
        Answer answer = Answer.answer(requestDto.getContent(), question);
        answerRepository.save(answer);

        if (question.getWriter() != null) {
            Member member = memberRepository.findByUsername(question.getWriter())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_MEMBER));
            if (member.canLogin())
                applicationEventPublisher.publishEvent(
                    NoticeEventAfterCommit
                        .builder()
                        .content(String.format("%s 문의의 답변이(가) 등록되었습니다.", question.getTitle()))
                        .owner(member)
                        .build()
                );
        }
        return true;
    }

    public AnswerDto findAnswer(Long questionId) {
        Answer answer = answerRepository.findByQuestionId(questionId)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_ANSWER));
        return AnswerDto.builder()
            .id(answer.getId())
            .content(answer.getContent())
            .createdDate(answer.getCreatedDate())
            .build();
    }
}
