package org.mint.smallcloud.label.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.pqc.math.linearalgebra.GoppaCode;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.repository.LabelRepository;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LabelThrowerService {
    private final LabelRepository labelRepository;
    public Label findByNameAndOwner(String labelName, Member member) {
        if(!labelRepository.existsByNameAndOwner(labelName, member))
            throw new ServiceException(ExceptionStatus.NOT_FOUND_LABEL);
        return labelRepository.findByNameAndOwner(labelName, member);
    }
}
