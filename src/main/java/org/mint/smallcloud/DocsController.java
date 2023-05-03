package org.mint.smallcloud;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs")
public class DocsController {
    @GetMapping
    public String docs() {
        return "docs/index.html";
    }
}
