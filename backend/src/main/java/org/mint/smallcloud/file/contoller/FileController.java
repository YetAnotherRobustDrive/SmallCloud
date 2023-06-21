package org.mint.smallcloud.file.contoller;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mint.smallcloud.ResponseDto;
import org.mint.smallcloud.bucket.dto.FileObjectDto;
import org.mint.smallcloud.bucket.service.StorageService;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.FileLocation;
import org.mint.smallcloud.file.domain.FileType;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.DirectoryMoveDto;
import org.mint.smallcloud.file.dto.FileDto;
import org.mint.smallcloud.file.dto.LabelUpdateDto;
import org.mint.smallcloud.file.dto.UsageDto;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.file.repository.FolderRepository;
import org.mint.smallcloud.file.service.FileFacadeService;
import org.mint.smallcloud.label.service.LabelService;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.domain.Roles;
import org.mint.smallcloud.user.repository.MemberRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    private final FileFacadeService fileFacadeService;

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

    @PostMapping("/mpd")
    public UploadResponse mpdUpload(
        @RequestParam("file") MultipartFile formFile,
        @RequestParam("originFileId") Long originFileId,
        HttpServletRequest request
    ) {
        UserDetails user = getLoginUser();
        String userName = user.getUsername();
        Member member = memberRepository.findByUsername(userName)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
        File originFile = fileRepository.findById(originFileId)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_FILE));

        String fileName = formFile.getOriginalFilename();
        long fileSize = formFile.getSize();
        String mimeType = request.getServletContext().getMimeType(fileName);
        if (mimeType == null)
            mimeType = "application/octet-stream";
        FileObjectDto fileObject;
        try {
           fileObject = storageService.uploadFile(formFile.getInputStream(), mimeType, fileSize);
        } catch (Exception e) {
            throw new ServiceException(ExceptionStatus.FILE_FAIL);
        }
        fileFacadeService.saveIndexData(fileObject, originFile, member);
        return UploadResponse.builder()
            .id(originFileId)
            .name(originFile.getName())
            .shared(false)
            .size(originFile.getSize())
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
        if (!file.canAccessUser(member))
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

        MediaType mediaType = MediaType.valueOf("application/dash+xml");

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(stream.available())
            .contentType(mediaType)
            .body(new InputStreamResource(stream));
    }

    @GetMapping("{fileId}/mpd")
    public ResponseEntity<Resource> downloadMpd(@PathVariable("fileId") Long fileId) {
        UserDetails user = getLoginUser();
        String userName = user.getUsername();
        Member member = memberRepository.findByUsername(userName)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NOT_FOUND_FILE));

        // authority
        if (!file.canAccessUser(member))
            throw new ServiceException(ExceptionStatus.NO_PERMISSION);
        if (file.getIndexData() == null)
            throw new ServiceException(ExceptionStatus.NOT_FOUND_FILE);
        try {
            InputStream stream = storageService.downloadFile(file.getIndexData().getLocation());
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
        } catch (Exception e) {
            throw new ServiceException(ExceptionStatus.FILE_FAIL);
        }
    }

    @PostMapping("/update/label")
    public ResponseEntity<?> updateLabel(@RequestBody LabelUpdateDto labelUpdateDto) {
        String userName = userDetailsProvider
                .getUserDetails().orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION)).getUsername();
        labelService.updateFile(labelUpdateDto, userName);
        return  ResponseEntity.ok().build();
    }

    @Secured(Roles.S_COMMON)
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{fileId}/restore")
    public void restore(@PathVariable("fileId") Long fileId) {
        UserDetails user = getLoginUser();
        fileFacadeService.restore(fileId, user.getUsername());
    }

    @Secured(Roles.S_COMMON)
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{fileId}/delete")
    public void delete(@PathVariable("fileId") Long fileId) {
        UserDetails user = getLoginUser();
        fileFacadeService.delete(fileId, user.getUsername());
    }

    @Secured(Roles.S_COMMON)
    @PostMapping("/{fileId}/move")
    public void move(@PathVariable("fileId") Long fileId,
                     @Valid @RequestBody DirectoryMoveDto directoryMoveDtoDto) {
        UserDetails user = getLoginUser();
        fileFacadeService.move(fileId, directoryMoveDtoDto, user.getUsername());
    }

    @Secured(Roles.S_COMMON)
    @PostMapping("/{fileId}/purge")
    public void purge(@PathVariable("fileId") Long fileId) throws Exception {
        UserDetails userDetails = getLoginUser();
        fileFacadeService.purge(fileId, userDetails.getUsername());
    }

    @Secured(Roles.S_COMMON)
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{fileId}/favorite")
    public void favorite(@PathVariable("fileId") Long fileId) {
        UserDetails user = getLoginUser();
        fileFacadeService.favorite(fileId, user.getUsername());
    }

    @Secured(Roles.S_COMMON)
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{fileId}/unfavorite")
    public void unFavorite(@PathVariable("fileId") Long fileId) {
        UserDetails user = getLoginUser();
        fileFacadeService.unFavorite(fileId, user.getUsername());
    }

    @Secured(Roles.S_COMMON)
    @GetMapping("/search")
    public ResponseDto<List<FileDto>> search(@RequestParam("q") String q) {
        UserDetails user = getLoginUser();
        List<FileDto> files = fileFacadeService.search(q, user.getUsername());
        return ResponseDto.<List<FileDto>>builder()
                .result(files)
                .build();
    }


    private String encode(String fileName) {
        try {
            return URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        } catch(Exception e) {
            return fileName;
        }
    }
    
    private UserDetails getLoginUser() {
        return userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
    }

    @Secured(Roles.S_COMMON)
    @GetMapping("/usage")
    public UsageDto getUsage() {
        UserDetails user = getLoginUser();
        UsageDto usage = fileFacadeService.getUsage(user.getUsername());
        return UsageDto.builder()
                .used(usage.getUsed())
                .build();
    }
}
