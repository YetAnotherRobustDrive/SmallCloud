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
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    private final DataNodeRepository dataNodeRepository;
    private final MemberThrowerService memberThrowerService;


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

    public void createDefaultLabels(Member member) {
        List<String> initLabels = new ArrayList<>();
        initLabels.add(defaultLabelType.systemDefaultFavorite.toString());
        initLabels.add(defaultLabelType.systemDefaultFinal.toString());
        initLabels.add(defaultLabelType.systemDefaultDraft.toString());
        initLabels.add(defaultLabelType.systemDefaultExpiration.toString());
        initLabels.add(defaultLabelType.systemDefaultPublic.toString());
        initLabels.add(defaultLabelType.systemDefaultSecurity.toString());
        initLabels.add(defaultLabelType.systemDefaultTrash.toString());
        initLabels.add(defaultLabelType.systemDefaultSensitivity.toString());

        initLabels.forEach(e -> {
            Label label = Label.of(e, member);
            labelRepository.save(label);
        });
    }

    public void addTrash(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultTrash.toString(), member);
        dataNode.addLabel(label);
    }
    public void deleteTrash(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultTrash.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void addFavorite(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultFavorite.toString(), member);
        dataNode.addLabel(label);
    }
    public void deleteFavorite(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultFavorite.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void addDraft(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultDraft.toString(), member);
        dataNode.addLabel(label);
    }
    public void deleteDraft(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultDraft.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void addExpiration(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultExpiration.toString(), member);
        dataNode.addLabel(label);
    }
    public void deleteExpiration(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultExpiration.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void addFinal(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultFinal.toString(), member);
        dataNode.addLabel(label);
    }
    public void deleteFinal(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultFinal.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void addSecurity(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultSecurity.toString(), member);
        dataNode.addLabel(label);
    }
    public void deleteSecurity(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultSecurity.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void addOpen(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultPublic.toString(), member);
        dataNode.addLabel(label);
    }
    public void deleteOpen(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultPublic.toString(), member);
        dataNode.deleteLabel(label);
    }
    public void addSensitivity(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultSensitivity.toString(), member);
        dataNode.addLabel(label);
    }
    public void deleteSensitivity(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(defaultLabelType.systemDefaultSensitivity.toString(), member);
        dataNode.deleteLabel(label);
    }

    public enum defaultLabelType {
        // PREFIX
        systemDefaultTrash,
        systemDefaultFavorite,
        systemDefaultDraft,
        systemDefaultExpiration,
        systemDefaultFinal,
        systemDefaultSecurity,
        systemDefaultPublic,
        systemDefaultSensitivity
    }
}