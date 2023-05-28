package org.mint.smallcloud.label.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.ResponseDto;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.label.service.LabelService;
import org.mint.smallcloud.user.domain.Roles;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/labels")
@Slf4j
@RequiredArgsConstructor
@Validated
public class LabelController {
    private final LabelService labelService;

    // 라벨 생성
    @Secured({Roles.S_COMMON})
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody LabelDto labelDto) {
        labelService.register(labelDto);
        return ResponseEntity.ok().build();
    }

    // 사용자가 만들었던 라벨 영구 제거
    @Secured({Roles.S_COMMON})
    @PostMapping("/deregister")
    public void deregister(@Valid @RequestBody LabelDto labelDto) {
        labelService.deregister(labelDto);
    }

    // 파일에서의 라벨 제거
    @Secured({Roles.S_COMMON})
    @PostMapping("/remove")
    public void remove(@Valid @RequestBody LabelDto labelDto) {
        labelService.remove(labelDto);
    }

//    // 파일에 라벨 등록
//    @Secured({Roles.S_COMMON})
//    @PostMapping("/attach")
//    public ResponseEntity<?> attach(@Valid @RequestBody )

    // 라벨 검색
    @Secured({Roles.S_COMMON})
    @GetMapping("/search")
    public LabelDto search(String partLabel) {
        return labelService.findLabel(partLabel);
    }
}
