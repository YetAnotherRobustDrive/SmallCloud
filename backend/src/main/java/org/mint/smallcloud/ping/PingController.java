package org.mint.smallcloud.ping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
@RequiredArgsConstructor
public class PingController {
    private final PingService pingService;

    @GetMapping
    public String ping() {
        return pingService.ping();
    }
}
