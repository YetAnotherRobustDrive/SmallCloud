package org.mint.smallcloud.ping;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PingFacadeService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PingFacadeService.class);

    public String ping() {
        return "pong";
    }
}
