package org.mint.smallcloud.label.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.label.repository.LabelRepository;
import org.mint.smallcloud.user.domain.Member;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabelThrowerService {
    private final LabelRepository labelRepository;

    public void checkExistsByLabelName(String labelName, Member owner) {
        if(labelRepository.existsByNameAndOwner(labelName, owner))
            throw new ServiceException(ExceptionStatus.ALREADY_EXISTS_LABEL);
    }

    public void checkNotExistsByLabelName(String labelName, Member owner) {
        if(!labelRepository.existsByNameAndOwner(labelName, owner))
            throw new ServiceException(ExceptionStatus.NOT_FOUND_LABEL);
    }
}
