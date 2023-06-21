package org.mint.smallcloud.file.service;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.bucket.dto.FileObjectDto;
import org.mint.smallcloud.bucket.service.StorageService;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.contoller.SegmentController;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.IndexData;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.file.repository.IndexDataRepository;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Transactional
public class SegmentService {
    private final IndexDataRepository indexDataRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;
    private final StorageService storageService;

    public SegmentController.UploadResponse createIndexData(String userName, Long fileId, MultipartFile formFile) {
        Member member = memberRepository.findByUsername(userName)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_FILE));
        if (!file.canAccessUser(member))
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);

        String mimeType = "application/dash+xml";

        try {
            String contents = new String(formFile.getBytes(), StandardCharsets.UTF_8);
            String modified = insertBaseURL(contents, String.format("/segments/%d/", file.getId()));
            byte[] modifiedBytes = modified.getBytes();
            long size = modifiedBytes.length;
            InputStream stream = new ByteArrayInputStream(modifiedBytes);
            FileObjectDto fileObject = storageService
                .uploadFile(stream, mimeType, size);
            String location = fileObject.getObjectId();
            indexDataRepository.save(IndexData.of(file, location, size));

            return SegmentController.UploadResponse.builder()
                .id(file.getId())
                .name(file.getName())
                .shared(false)
                .size(file.getSize())
                .thumbnail("")
                .securityLevel("")
                .type(mimeType)
                .writingStage("")
                .build();
        } catch (Exception e) {
            throw new ServiceException(ExceptionStatus.FILE_FAIL);
        }
    }


    private String insertBaseURL(String manifest, String baseUrl) {
        int i = manifest.indexOf("<MPD");
        if (i == -1) // illegal manifest
            return null;
        for (; i < manifest.length(); i++)
            if (manifest.charAt(i) == '>')
                break;
        if (i == manifest.length())  // illegal manifest
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append(manifest.substring(0, i+1));
        sb.append(String.format("<BaseURL>%s</BaseURL>", baseUrl));
        sb.append(manifest.substring(i + 1));
        return sb.toString();
    }
}
