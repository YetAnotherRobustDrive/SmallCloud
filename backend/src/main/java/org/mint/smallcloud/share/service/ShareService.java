package org.mint.smallcloud.share.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.share.dto.ShareRequestDto;
import org.mint.smallcloud.share.repository.GroupShareRepository;
import org.mint.smallcloud.share.repository.MemberShareRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShareService {

    private final GroupShareRepository groupShareRepository;
    private final MemberShareRepository memberShareRepository;
    public void create(String loginUsername, ShareRequestDto dto) {
        // TODO: file이 완성된 후 구현
    }

    public void delete(String loginUsername, ShareRequestDto dto) {
        // TODO:
    }
}
