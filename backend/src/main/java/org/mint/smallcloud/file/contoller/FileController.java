package org.mint.smallcloud.file.contoller;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.bucket.dto.FileObjectDto;
import org.mint.smallcloud.bucket.service.StorageService;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.file.domain.FileType;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.file.repository.FolderRepository;
import org.mint.smallcloud.label.service.LabelService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final StorageService storageService;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final UserDetailsProvider userDetailsProvider;
    private final MemberRepository memberRepository;
    private final LabelService labelService;

    @Builder
    @Getter
    public static class UploadResponse {
        Long id;
        String securityLevel;
        String writingStage;
        String name;
        Long size;
        String type;
        String thumbnail;
        boolean shared;
    }
    
    @PostMapping
    public UploadResponse upload(@RequestParam("file") MultipartFile formFile,
                                 @RequestParam("cwd") Long dirId,
                                 HttpServletRequest request) throws Exception {
        UserDetails user = getLoginUser();
        String userName = user.getUsername();
        Optional<Member> memberOpt = memberRepository.findByUsername(userName);
        if (memberOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "auth/forbidden");

        Optional<Folder> folderOpt = folderRepository.findById(dirId);
        if (folderOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "directory/forbidden");
        Folder folder = folderOpt.get();

        // authority
        if (!folder.getAuthor().equals(memberOpt.get()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "auth/forbidden");            
        
        String fileName = formFile.getOriginalFilename();
        Long fileSize = formFile.getSize();
        String mimeType = request.getServletContext().getMimeType(fileName);
        if (mimeType == null) 
            mimeType = "application/octet-stream";
        FileObjectDto fileObject = storageService.uploadFile(formFile.getInputStream(), mimeType, fileSize);
        
        File file = File.of(folder, FileType.of(fileName, mimeType),
                            FileLocation.of(fileObject.getObjectId()),
                            fileSize, memberOpt.get());

        file = fileRepository.save(file);
        return UploadResponse.builder()
            .id(file.getId())
            .name(file.getName())
            .shared(false)
            .size(file.getSize())
            .thumbnail("")
            .securityLevel("")
            .type(mimeType)
            .writingStage("")
            .build();
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") Long fileId) throws Exception {
        UserDetails user = getLoginUser();
        String userName = user.getUsername();
        Optional<Member> memberOpt = memberRepository.findByUsername(userName);
        if (memberOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "auth/forbidden");
        Member member = memberOpt.get();

        Optional<File> fileOpt = fileRepository.findById(fileId);
        if (fileOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "file/forbidden");
        File file = fileOpt.get();

        // authority
        if (!file.getAuthor().equals(member))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "auth/forbidden");

        InputStream stream = storageService.downloadFile(file.getLocation().getLocation());
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    String.format("attachment; filename=\"%s\"",
                                  encode(file.getName())));
        headers.add(HttpHeaders.CACHE_CONTROL,
                    "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        MediaType mediaType = MediaType.parseMediaType(file.getFileType().getType());

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(file.getSize())
            .contentType(mediaType)
            .body(new InputStreamResource(stream));
    }

    @PostMapping("/{fileId}/update/label")
    public ResponseEntity<?> updateLabel(@RequestParam List<String> labels, @RequestParam("fileId") Long fileId) {
        String userName = userDetailsProvider
                .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        labelService.updateFile(fileId, labels, userName);
        return  ResponseEntity.ok().build();
    }

    private String encode(String fileName) {
        try {
            return URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        } catch(Exception e) {
            return fileName;
        }
    }

    
    private UserDetails getLoginUser() {
        return userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
    }
}
