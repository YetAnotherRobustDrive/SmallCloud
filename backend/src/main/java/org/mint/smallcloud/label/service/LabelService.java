package org.mint.smallcloud.label.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.dto.LabelUpdateDto;
import org.mint.smallcloud.file.repository.DataNodeRepository;

import org.mint.smallcloud.label.domain.Label;

import org.mint.smallcloud.label.repository.LabelRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.UserLabelDto;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    private final LabelThrowerService labelThrowerService;
    private final DataNodeRepository dataNodeRepository;
    private final MemberThrowerService memberThrowerService;

    public void register(String labelName, UserLabelDto userLabelDto, DataNode dataNode, Member member) {
        if (userLabelDto==null)
            throw new ServiceException(ExceptionStatus.NOT_FOUND_OWNER);
        Label label = Label.of(
                labelName,
                member);
        label.addFile(dataNode);
        if(!labelThrowerService.checkExistsByLabelName(labelName, member))
            labelRepository.save(label);
    }


    public void remove(String labelName, DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(labelName, member);
        label.deleteFile(dataNode);
    }

    /**
     * 파일 : 라벨1, 라벨2, 라벨3
     * temp = 파일
     * labels : 라벨1, new1, new2

     * 파일 - labels : 라벨2, 라벨3 (빼야 하는거)
     * labels - temp : new1, new2 (더해야 하는거)
     */

    public void updateFile(LabelUpdateDto labelUpdateDto, String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        DataNode dataNode = dataNodeRepository.findById(labelUpdateDto.getFileId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
        dataNode.getLabels().stream()
                .filter(label -> !labelUpdateDto.getLabels().contains(label.getName()))
                .forEach(label -> remove(label.getName(), dataNode, member));
        List<String> tempLabelsName = dataNode.getLabels().stream().map(Label::getName).collect(Collectors.toList());
        labelUpdateDto.getLabels().stream()
                .filter(label -> !tempLabelsName.contains(label))
                .forEach(label -> {
                    UserLabelDto userLabelDto = UserLabelDto.builder()
                            .nickname(member.getNickname())
                            .username(member.getUsername())
                            .build();
                    register(label, userLabelDto, dataNode, member);
                });
    }
}
