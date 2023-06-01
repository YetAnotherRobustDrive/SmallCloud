package org.mint.smallcloud.label.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.dto.DataNodeLabelDto;
import org.mint.smallcloud.file.repository.DataNodeRepository;

import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.label.dto.LabelFilesDto;
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

    public void register(LabelDto labelDto, String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        if (labelDto.getFile() == null)
            throw new ServiceException(ExceptionStatus.FILE_NOT_FOUND);
        if (labelDto.getOwner()==null)
            throw new ServiceException(ExceptionStatus.NOT_FOUND_OWNER);
        DataNode dataNode = dataNodeRepository.findById(labelDto.getFile().getId())
                        .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
        labelThrowerService.checkExistsByLabelName(labelDto.getName(), member);
        Label label = Label.of(
                labelDto.getName(),
                member);
        label.addFile(dataNode);
        labelRepository.save(label);
    }

    public void deregister(LabelDto labelDto, String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        labelThrowerService.checkNotExistsByLabelName(labelDto.getName(), member);
        Label label = labelRepository.findByNameAndOwner(labelDto.getName(), member);
        List<DataNode> dataNodeFiles = dataNodeRepository.findByAuthor(member);
        dataNodeFiles.forEach(s -> s.deleteLabel(label));
        labelRepository.delete(label);
    }

    public void remove(LabelDto labelDto, String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        DataNode dataNode = dataNodeRepository.findById(labelDto.getFile().getId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
        labelThrowerService.checkNotExistsByLabelName(labelDto.getName(), member);
        Label label = labelRepository.findByNameAndOwner(labelDto.getName(), member);
        label.deleteFile(dataNode);
    }

    public void attach(LabelDto labelDto, String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        if (labelDto.getFile() == null)
            throw new ServiceException(ExceptionStatus.FILE_NOT_FOUND);
        if (labelDto.getOwner()==null)
            throw new ServiceException(ExceptionStatus.NOT_FOUND_OWNER);
        DataNode dataNode = dataNodeRepository.findById(labelDto.getFile().getId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));
        labelThrowerService.checkExistsByLabelName(labelDto.getName(), member);
        Label label = Label.of(
                labelDto.getName(),
                member);
        label.addFile(dataNode);
    }

    public LabelFilesDto findLabel(String labelName, String userName) {

        Member member = memberThrowerService.getMemberByUsername(userName);
        labelThrowerService.checkNotExistsByLabelName(labelName, member);
        Label label = labelRepository.findByNameAndOwner(labelName, member);
        List<DataNode> dataNode = label.getFiles();
        List<DataNodeLabelDto> dataNodeLabelDtos = new ArrayList<>();
        dataNode.forEach(e -> {
            DataNodeLabelDto dataNodeLabelDto = DataNodeLabelDto.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .createdDate(e.getCreatedDate())
                    .parentFolderId(e.getParentFolder().getId())
                    .build();
            dataNodeLabelDtos.add(dataNodeLabelDto);
        });

        UserLabelDto userLabelDto = UserLabelDto.builder()
                .nickname(member.getNickname())
                .username(member.getUsername())
                .build();
        return LabelFilesDto.builder()
                .name(label.getName())
                .owner(userLabelDto)
                .file(dataNodeLabelDtos)
                .build();
    }
}
