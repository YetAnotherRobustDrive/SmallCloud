package org.mint.smallcloud.ping;

import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.log.user.UserBehaviorLogging;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class PingService {
    @UserBehaviorLogging("ping")
    public String ping() {
        return "pong";
    }
}
