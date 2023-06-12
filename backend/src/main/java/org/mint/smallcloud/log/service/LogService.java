package org.mint.smallcloud.log.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.log.dto.RequestLogDto;
import org.mint.smallcloud.log.dto.ResponseLogDto;
import org.mint.smallcloud.log.mapper.LogMapper;
import org.mint.smallcloud.log.user.UserLog;
import org.mint.smallcloud.log.user.UserLogRepository;
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
    private final LogMapper logMapper;

    public List<ResponseLogDto> findLogs(RequestLogDto requestLogDto) {
        List<UserLog> userLogs = userLogRepository.findLogs(
                requestLogDto.getNickName(),
                requestLogDto.getStartTime(),
                requestLogDto.getEndTime(),
                requestLogDto.getStatus(),
                requestLogDto.getAction());
        return userLogs.stream()
                .map(logMapper::toResponseLogDto)
                .collect(Collectors.toList());
    }
}
