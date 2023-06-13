package org.mint.smallcloud.log.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.log.dto.RequestLogDto;
import org.mint.smallcloud.log.dto.ResponseLogDto;
import org.mint.smallcloud.log.dto.ResponseLoginLogDto;
import org.mint.smallcloud.log.mapper.LogMapper;
import org.mint.smallcloud.log.user.UserLog;
import org.mint.smallcloud.log.user.UserLogRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LogService {
    private final UserLogRepository userLogRepository;
    private final MemberThrowerService memberThrowerService;
    private final LogMapper logMapper;

    public List<ResponseLogDto> findLogs(RequestLogDto requestLogDto, Pageable pageable) {
        List<UserLog> userLogs = userLogRepository.findLogs(
                requestLogDto.getNickName(),
                requestLogDto.getStartTime(),
                requestLogDto.getEndTime(),
                requestLogDto.getStatus(),
                requestLogDto.getAction(),
                pageable);
        return userLogs.stream()
                .map(logMapper::toResponseLogDto)
                .collect(Collectors.toList());
    }


    public List<ResponseLoginLogDto> findLoginLogsByUser(String username) {
        List<UserLog> userLogs = userLogRepository.findByActionStartsWith("/ping/login/" + username + "/");
        List<UserLog> formatted = userLogs.stream()
                .map(e -> {
                    Boolean status = e.getAction().contains("success") ? true : false;
                    String action = e.getAction().replace("/ping/", "").replace("success", "").replace("fail", "");
                    return UserLog.of(
                            e.getMember(),
                            e.getTime(),
                            action,
                            e.getIpAddr(),
                            status
                    );
                })
                .collect(Collectors.toList());
        return formatted.stream()
                .map(logMapper::toResponseLoginLogDto)
                .collect(Collectors.toList());
    }
}
