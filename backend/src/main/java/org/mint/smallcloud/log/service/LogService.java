package org.mint.smallcloud.log.service;

import org.mint.smallcloud.log.dto.RequestLogDto;
import org.mint.smallcloud.log.dto.ResponseLogDto;
import org.mint.smallcloud.log.dto.ResponseLoginLogDto;
import org.mint.smallcloud.log.mapper.LogMapper;
import org.mint.smallcloud.log.user.UserLog;
import org.mint.smallcloud.log.user.UserLogRepository;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LogService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LogService.class);
    private final UserLogRepository userLogRepository;
    private final LogMapper logMapper;

    public LogService(UserLogRepository userLogRepository, LogMapper logMapper) {
        this.userLogRepository = userLogRepository;
        this.logMapper = logMapper;
    }

    public Page<ResponseLogDto> findLogs(RequestLogDto requestLogDto, Pageable pageable) {
        Page<UserLog> userLogs = userLogRepository.findLogs(
            requestLogDto.getUserName(),
            requestLogDto.getStartTime(),
            requestLogDto.getEndTime(),
            requestLogDto.getStatus(),
            requestLogDto.getAction(),
            pageable);

        return userLogs.map(m ->
        {
            String nickname = m.getMember() == null ? null : m.getMember().getUsername();
            return ResponseLogDto.builder()
                .action(m.getAction())
                .localDateTime(m.getTime())
                .nickName(nickname)
                .status(m.getStatus())
                .ipAddr(m.getIpAddr())
                .build();
        });
    }


    public List<ResponseLoginLogDto> findLoginLogsByUser(String username) {
        List<UserLog> userLogs = userLogRepository.findByActionStartsWith("/ping/login/" + username + "/");
        List<UserLog> formatted = userLogs.stream()
            .map(e -> {
                Boolean status = e.getAction().contains("success");
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
