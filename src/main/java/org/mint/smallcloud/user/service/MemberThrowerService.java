package org.mint.smallcloud.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Role;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberThrowerService {
    private final MemberRepository memberRepository;

    public Member getCommonByUsername(String username) {
        Member member = memberRepository
            .findByUsername(username)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_COMMON_MEMBER));
        if (member.isRole(Role.COMMON))
            return member;
        throw new ServiceException(ExceptionStatus.NOT_FOUND_COMMON_MEMBER);
    }

    public Member getMemberByUsername(String username) {
        return memberRepository
            .findByUsername(username)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_MEMBER));
    }

    public void checkExistsByUsername(String username) {
        if (memberRepository.existsByUsername(username))
            throw new ServiceException(ExceptionStatus.USERNAME_ALREADY_EXISTS);
    }

    public void checkPassword(String id, String password) {
        Member member = getMemberByUsername(id);
        if (!member.verifyPassword(password))
            throw new ServiceException(ExceptionStatus.WRONG_PASSWORD);
    }
}
