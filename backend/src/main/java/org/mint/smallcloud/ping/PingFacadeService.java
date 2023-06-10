package org.mint.smallcloud.ping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class PingFacadeService {
    public String ping() {
        return "pong";
    }
}
