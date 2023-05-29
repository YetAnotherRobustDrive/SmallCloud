package org.mint.smallcloud.label.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.label.repository.LabelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabelThrowerService {
    private final LabelRepository labelRepository;

    public void checkExistsByLabelName(String labelName) {
        if(labelRepository.existsByName(labelName))
            throw new ServiceException(ExceptionStatus.ALREADY_EXISTS_LABEL);
    }

    public void checkNotExistsByLabelName(String labelName) {
        if(!labelRepository.existsByName(labelName))
            throw new ServiceException(ExceptionStatus.NOT_FOUND_LABEL);
    }
}
