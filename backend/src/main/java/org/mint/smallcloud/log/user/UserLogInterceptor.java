package org.mint.smallcloud.log.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLogInterceptor implements HandlerInterceptor {
    private final UserDetailsProvider userDetailsProvider;
    private final MemberRepository memberRepository;
    private final UserLogRepository userLogRepository;
    private static final List<String> IP_HEADERS = Arrays.asList("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("intercept postHandle");
        Member member = getLoginMember();
        String ip = getClientIpAddr(request);
        String action = request.getRequestURI();
        LocalDateTime now = LocalDateTime.now();
        Boolean status = response.getStatus() == 200;
        UserLog userLog = UserLog.of(member, now, action, ip, status);
        userLogRepository.saveAndFlush(userLog);
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        return IP_HEADERS.stream()
            .map(request::getHeader)
            .filter(Objects::nonNull)
            .filter(ip -> !ip.isEmpty() && !ip.equalsIgnoreCase("unknown"))
            .findFirst()
            .orElseGet(request::getRemoteAddr);
    }

    private Member getLoginMember() {
        Optional<UserDetails> userDetails = userDetailsProvider.getUserDetails();
        return userDetails
            .flatMap(details -> (memberRepository.findByUsername(details.getUsername())))
            .orElse(null);
    }
}
