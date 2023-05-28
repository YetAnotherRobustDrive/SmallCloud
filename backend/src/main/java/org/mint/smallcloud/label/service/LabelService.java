package org.mint.smallcloud.label.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.label.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    private final LabelThrowerService labelThrowerService;

    public void register(LabelDto labelDto) {
        labelThrowerService.checkExistsByLabelName(labelDto.getName());
        Label label = Label.of(
                labelDto.getName(),
                labelDto.getOwner());
        label.addFile(labelDto.getFile());
        labelRepository.save(label);
    }

    public void deregister(LabelDto labelDto) {
        remove(labelDto);
        Label label = labelRepository.findByName(labelDto.getName());
        labelRepository.delete(label);
    }

    public void remove(LabelDto labelDto) {
        labelThrowerService.checkNotExistsByLabelName(labelDto.getName());
        Label label = labelRepository.findByName(labelDto.getName());
        label.deleteFile(labelDto.getFile());
    }

    public LabelDto findLabel(String partLabel) {
        Label label = labelRepository.findByName(partLabel);
        return LabelDto.builder()
                .name(label.getName())
                .owner(label.getOwner())
                .build();
    }
}
