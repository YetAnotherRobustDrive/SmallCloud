package org.mint.smallcloud.label.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.repository.DataNodeRepository;

import org.mint.smallcloud.label.domain.Label;

import org.mint.smallcloud.label.repository.LabelRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.UserLabelDto;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public void updateFile(Long fileId, List<String> labels, String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        DataNode dataNode = dataNodeRepository.findById(fileId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
        List<Label> labelList = new ArrayList<>();
        labels.forEach(e -> {
            labelList.add(labelRepository.findByNameAndOwner(e, member));
        });
        List<Label> tempLabels = dataNode.getLabels();
        // delete
        dataNode.getLabels().removeAll(labelList);
        dataNode.getLabels().forEach(s -> {
            remove(s.getName(), dataNode, member);
        });
        // register
        labelList.removeAll(tempLabels);
        labelList.forEach(e -> {
            UserLabelDto userLabelDto = UserLabelDto.builder()
                    .nickname(member.getNickname())
                    .username(member.getUsername())
                    .build();
            register(e.getName(), userLabelDto, dataNode, member);
        });
    }
}
