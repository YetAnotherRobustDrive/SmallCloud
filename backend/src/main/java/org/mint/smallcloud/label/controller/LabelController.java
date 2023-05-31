package org.mint.smallcloud.label.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.ResponseDto;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.label.dto.LabelFilesDto;
import org.mint.smallcloud.label.service.LabelService;
import org.mint.smallcloud.security.UserDetailsProvider;
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
    private final UserDetailsProvider userDetailsProvider;

    // 라벨 생성
    @Secured({Roles.S_COMMON})
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody LabelDto labelDto) {
        String userName = userDetailsProvider
                .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        labelService.register(labelDto, userName);
        return ResponseEntity.ok().build();
    }

    // 사용자가 만들었던 라벨 영구 제거
    @Secured({Roles.S_COMMON})
    @PostMapping("/deregister")
    public ResponseEntity<?> deregister(@Valid @RequestBody LabelDto labelDto) {
        String userName = userDetailsProvider
                .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        labelService.deregister(labelDto, userName);
        return ResponseEntity.ok().build();
    }

    // 파일에서의 라벨 제거
    @Secured({Roles.S_COMMON})
    @PostMapping("/remove")
    public ResponseEntity<?> remove(@Valid @RequestBody LabelDto labelDto) {
        String userName = userDetailsProvider
                .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        labelService.remove(labelDto, userName);
        return ResponseEntity.ok().build();
    }

    // 파일에 라벨 등록
    @Secured({Roles.S_COMMON})
    @PostMapping("/attach")
    public ResponseEntity<?> attach(@Valid @RequestBody LabelDto labelDto) {
        String userName = userDetailsProvider
                .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        labelService.attach(labelDto, userName);
        return ResponseEntity.ok().build();
    }

    // 라벨 검색
//    @Secured({Roles.S_COMMON})
//    @GetMapping("/search")
//    public LabelFilesDto search(String partLabel) {
//        String userName = userDetailsProvider
//                .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
//        return labelService.findLabel(partLabel, userName);
//    }

    /**
     * - todo (필요해 보이는)
     * 모든 파일의 하나의 라벨 모두 지우기
     * 하나의 파일의 라벨 모두 지우기
     * 휴지통 설정
     */
}
