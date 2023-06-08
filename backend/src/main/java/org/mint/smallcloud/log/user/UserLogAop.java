package org.mint.smallcloud.log.user;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Aspect
@RequiredArgsConstructor
public class UserLogAop {
    private final UserLogRepository userLogRepository;
    private final MemberRepository memberRepository;
    private final UserDetailsProvider userDetailsProvider;

    @AfterReturning("@annotation(UserBehaviorLogging)")
    @Transactional
    public void logUserBehavior(JoinPoint joinPoint) {
        Member member = getLoginMember();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UserBehaviorLogging annotation = method.getAnnotation(UserBehaviorLogging.class);
        UserLog userLog = UserLog.of(member, LocalDateTime.now(), annotation.value());
        userLogRepository.save(userLog);
    }

    private Member getLoginMember() {
        Optional<UserDetails> userDetails = userDetailsProvider.getUserDetails();
        Member member;
        member = userDetails
            .flatMap(details -> memberRepository.findByUsername(details.getUsername()))
            .orElse(null);
        return member;
    }
}