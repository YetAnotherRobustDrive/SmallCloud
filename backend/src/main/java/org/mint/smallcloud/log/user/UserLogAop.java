package org.mint.smallcloud.log.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class UserLogAop {
    private final UserLogRepository userLogRepository;
    private final MemberRepository memberRepository;
    private final UserDetailsProvider userDetailsProvider;

    private static final List<String> IP_HEADERS = Arrays.asList("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");

    @AfterReturning("@annotation(UserBehaviorLogging)")
    @Transactional
    public void logUserBehavior() {
        Member member = getLoginMember();
        ServletRequestAttributes attr =
            (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String url = request.getRequestURI();
        String ip = getClientIpAddr(request);
        UserLog userLog = UserLog.of(
            member,
            LocalDateTime.now(),
            url,
            ip
        );
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

    public static String getClientIpAddr(HttpServletRequest request) {
        return IP_HEADERS.stream()
            .map(request::getHeader)
            .filter(Objects::nonNull)
            .filter(ip -> !ip.isEmpty() && !ip.equalsIgnoreCase("unknown"))
            .findFirst()
            .orElseGet(request::getRemoteAddr);
    }

}