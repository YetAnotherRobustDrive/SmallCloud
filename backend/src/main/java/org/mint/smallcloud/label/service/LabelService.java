package org.mint.smallcloud.label.service;

import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.LabelUpdateDto;
import org.mint.smallcloud.file.mapper.FileMapper;
import org.mint.smallcloud.file.mapper.FolderMapper;
import org.mint.smallcloud.file.repository.DataNodeRepository;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.file.repository.FolderRepository;
import org.mint.smallcloud.label.domain.DefaultLabelType;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.dto.LabelFilesDto;
import org.mint.smallcloud.label.repository.LabelRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.service.MemberThrowerService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LabelService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LabelService.class);
    private final LabelRepository labelRepository;
    private final DataNodeRepository dataNodeRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final MemberThrowerService memberThrowerService;
    private final FileMapper fileMapper;
    private final FolderMapper folderMapper;

    public LabelService(LabelRepository labelRepository, DataNodeRepository dataNodeRepository, FileRepository fileRepository, FolderRepository folderRepository, MemberThrowerService memberThrowerService, FileMapper fileMapper, FolderMapper folderMapper) {
        this.labelRepository = labelRepository;
        this.dataNodeRepository = dataNodeRepository;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.memberThrowerService = memberThrowerService;
        this.fileMapper = fileMapper;
        this.folderMapper = folderMapper;
    }

    public Label register(Label label) {
        if (!labelRepository.existsByNameAndOwner(label.getName(), label.getOwner())) {
            labelRepository.save(label);
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
        for (Iterator<Label> iterator = labels.iterator(); iterator.hasNext(); ) {
            Label label = iterator.next();
            if (!labelUpdateDto.getLabels().contains(label.getName()) && label != labelRepository.findByNameAndOwner(DefaultLabelType.defaultFavorite.getLabelName(), member)) {
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
        if (!labelRepository.existsByNameAndOwner(labelName, member))
            throw new ServiceException(ExceptionStatus.NOT_FOUND_LABEL);
        List<File> files = fileRepository.findDataNodeByLabelNameAndOwner(labelName, userName);
        List<Folder> folders = folderRepository.findDataNodeByLabelNameAndOwner(labelName, userName);
        return LabelFilesDto.builder()
            .name(labelName)
            .files(files.stream().map(fileMapper::toFileDto).collect(Collectors.toList()))
            .folders(folders.stream().map(folderMapper::toDirectoryDto).collect(Collectors.toList()))
            .build();
    }

    public LabelFilesDto trash(String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        if (!labelRepository.existsByNameAndOwner(DefaultLabelType.defaultTrash.getLabelName(), member))
            throw new ServiceException(ExceptionStatus.NOT_FOUND_LABEL);
        List<File> files = fileRepository.findDataNodeByLabelNameAndOwner(DefaultLabelType.defaultTrash.getLabelName(), userName);
        List<Folder> folders = folderRepository.findDataNodeByLabelNameAndOwner(DefaultLabelType.defaultTrash.getLabelName(), userName);
        return LabelFilesDto.builder()
            .name(DefaultLabelType.defaultTrash.getLabelName())
            .files(files.stream().map(fileMapper::toFileDto).collect(Collectors.toList()))
            .folders(folders.stream().map(folderMapper::toDirectoryDto).collect(Collectors.toList()))
            .build();
    }

    public LabelFilesDto favorite(String userName) {
        Member member = memberThrowerService.getMemberByUsername(userName);
        if (!labelRepository.existsByNameAndOwner(DefaultLabelType.defaultFavorite.getLabelName(), member))
            throw new ServiceException(ExceptionStatus.NOT_FOUND_LABEL);
        List<File> files = fileRepository.findDataNodeByLabelNameAndOwner(DefaultLabelType.defaultFavorite.getLabelName(), userName);
        List<Folder> folders = folderRepository.findDataNodeByLabelNameAndOwner(DefaultLabelType.defaultFavorite.getLabelName(), userName);
        return LabelFilesDto.builder()
            .name(DefaultLabelType.defaultFavorite.getLabelName())
            .files(files.stream().map(fileMapper::toFileDto).collect(Collectors.toList()))
            .folders(folders.stream().map(folderMapper::toDirectoryDto).collect(Collectors.toList()))
            .build();
    }


    public void createDefaultLabels(Member member) {
        List<String> initLabels = new ArrayList<>();
        initLabels.add(DefaultLabelType.defaultFavorite.getLabelName());
        initLabels.add(DefaultLabelType.defaultTrash.getLabelName());

        initLabels.forEach(e -> {
            Label label = Label.of(e, member);
            labelRepository.save(label);
        });
    }

    public void attachTrash(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultTrash.getLabelName(), member);
        if (dataNode.getLabels().contains(label))
            throw new ServiceException(ExceptionStatus.ALREADY_EXISTS_LABEL);
        dataNode.addLabel(label);
    }

    public void detachTrash(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultTrash.getLabelName(), member);
        if (!dataNode.getLabels().contains(label))
            throw new ServiceException(ExceptionStatus.NOT_FOUND_LABEL);
        dataNode.deleteLabel(label);
    }

    public void attachFavorite(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultFavorite.getLabelName(), member);
        if (dataNode.getLabels().contains(label))
            throw new ServiceException(ExceptionStatus.ALREADY_EXISTS_LABEL);
        dataNode.addLabel(label);
    }

    public void detachFavorite(DataNode dataNode, Member member) {
        Label label = labelRepository.findByNameAndOwner(DefaultLabelType.defaultFavorite.getLabelName(), member);
        if (!dataNode.getLabels().contains(label))
            throw new ServiceException(ExceptionStatus.NOT_FOUND_LABEL);
        dataNode.deleteLabel(label);
    }
}