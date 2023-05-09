package org.mint.smallcloud.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.security.dto.LoginDto;
import org.mint.smallcloud.security.dto.RegisterDto;
import org.mint.smallcloud.security.dto.UserDetailsDto;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberThrowerService memberThrowerService;
    private final MemberRepository memberRepository;

    public UserDetailsDto getUserDetails(String username) {
        Member member = memberThrowerService.getMemberByUsername(username);
        return UserDetailsDto.builder()
            .username(member.getUsername())
            .password(member.getPassword())
            .roles(member.getRole())
            .disabled(member.isLocked())
            .build();
    }

    public void registerCommon(RegisterDto registerDto) {
        memberThrowerService.checkExistsByUsername(registerDto.getId());
        Member member = Member.of(
            registerDto.getId(),
            registerDto.getPassword(),
            registerDto.getName());
        memberRepository.save(member);
    }

    public void deregisterCommon(String username) {
        Member member = memberThrowerService.getCommonByUsername(username);
        memberRepository.delete(member);
    }

    public void checkPassword(LoginDto loginDto) {
        memberThrowerService.checkPassword(loginDto.getId(), loginDto.getPassword());
    }
}
