package org.mint.smallcloud.config;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.domain.Roles;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestAdmin {
    private final UserDetailsProvider userDetailsProvider;
    private final MemberRepository memberRepository;
    @Secured({Roles.S_COMMON, Roles.S_PRIVILEGE})
    @GetMapping("/admin")
    @Transactional
    public void admin() {
        UserDetails userDetails = userDetailsProvider
            .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        Member member = memberRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new ServiceException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        member.changeRole(Role.ADMIN);
    }
}
