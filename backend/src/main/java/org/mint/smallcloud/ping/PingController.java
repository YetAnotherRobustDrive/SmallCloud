package org.mint.smallcloud.ping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {
    private final PingFacadeService pingService;

    public PingController(PingFacadeService pingService) {
        this.pingService = pingService;
    }

    @GetMapping
    public String ping() {
        return pingService.ping();
    }
}
