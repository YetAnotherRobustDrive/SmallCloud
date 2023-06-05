package org.mint.smallcloud.label.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.dto.LabelUpdateDto;
import org.mint.smallcloud.file.mapper.DataNodeMapper;
import org.mint.smallcloud.file.repository.DataNodeRepository;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.domain.DefaultLabelType;
import org.mint.smallcloud.label.dto.LabelFilesDto;
import org.mint.smallcloud.label.repository.LabelRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LabelService {
    private final LabelRepository labelRepository;
    private final DataNodeRepository dataNodeRepository;
    private final MemberThrowerService memberThrowerService;
    private final LabelThrowerService labelThrowerService;
    private final DataNodeMapper dataNodeMapper;

    public Label register(Label label) {
        if(!labelRepository.existsByNameAndOwner(label.getName(), label.getOwner())) {
            labelRepository.save(label);
            labelRepository.flush();
        }
        return labelRepository.findByNameAndOwner(label.getName(), label.getOwner());
    }

    public void remove(String labelName, DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(labelName, member);
        label.deleteFile(dataNode);
    }

    public void updateFile(LabelUpdateDto labelUpdateDto, String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        DataNode dataNode = dataNodeRepository.findById(labelUpdateDto.getFileId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.FILE_NOT_FOUND));

        List<Label> labels = dataNode.getLabels();
        for (Iterator<Label> iterator = labels.iterator(); iterator.hasNext();) {
            Label label = iterator.next();
            if (!labelUpdateDto.getLabels().contains(label.getName())) {
                iterator.remove();
                remove(label.getName(), dataNode, member);
            }
        }
        List<String> tempLabelsName = dataNode.getLabels().stream().map(Label::getName).collect(Collectors.toList());
        labelUpdateDto.getLabels().stream()
                .filter(label -> !tempLabelsName.contains(label))
                .forEach(label -> {
                    Label labelSaved = register(Label.of(label, member));
                    labelSaved.addFile(dataNode);
                });
    }
    public LabelFilesDto search(String labelName, String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        labelThrowerService.findByNameAndOwner(labelName, member);
        List<DataNode> dataNode = dataNodeRepository.findDataNodeByLabelName(labelName);
        return LabelFilesDto.builder()
                .name(labelName)
                .files(dataNode.stream().map(dataNodeMapper::toDataNodeDto).collect(Collectors.toList()))
                .build();
    }

    public void createDefaultLabels(Member member) {
        List<String> initLabels = new ArrayList<>();
        initLabels.add(DefaultLabelType.defaultFavorite.toString());
        initLabels.add(DefaultLabelType.defaultFinal.toString());
        initLabels.add(DefaultLabelType.defaultDraft.toString());
        initLabels.add(DefaultLabelType.defaultExpiration.toString());
        initLabels.add(DefaultLabelType.defaultPublic.toString());
        initLabels.add(DefaultLabelType.defaultSecurity.toString());
        initLabels.add(DefaultLabelType.defaultTrash.toString());
        initLabels.add(DefaultLabelType.defaultSensitivity.toString());

        initLabels.forEach(e -> {
            Label label = Label.of(e, member);
            labelRepository.save(label);
        });
    }

    public void attachTrash(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultTrash.toString(), member);
        dataNode.addLabel(label);
    }
    public void detachTrash(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultTrash.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void attachFavorite(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultFavorite.toString(), member);
        dataNode.addLabel(label);
    }
    public void detachFavorite(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultFavorite.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void attachDraft(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultDraft.toString(), member);
        dataNode.addLabel(label);
    }
    public void detachDraft(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultDraft.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void attachExpiration(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultExpiration.toString(), member);
        dataNode.addLabel(label);
    }
    public void detachExpiration(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultExpiration.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void attachFinal(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultFinal.toString(), member);
        dataNode.addLabel(label);
    }
    public void detachFinal(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultFinal.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void attachSecurity(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultSecurity.toString(), member);
        dataNode.addLabel(label);
    }
    public void detachSecurity(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultSecurity.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void attachPublic(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultPublic.toString(), member);
        dataNode.addLabel(label);
    }
    public void detachPublic(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultPublic.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void attachSensitivity(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultSensitivity.toString(), member);
        dataNode.addLabel(label);
    }
    public void detachSensitivity(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultSensitivity.toString(), member);
        dataNode.deleteLabel(label);
    }
}