package org.mint.smallcloud.file.contoller;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.bucket.dto.FileObjectDto;
import org.mint.smallcloud.bucket.service.StorageService;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.FileType;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.domain.Segment;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.file.repository.FolderRepository;
import org.mint.smallcloud.file.repository.SegmentRepository;
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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/segments")
public class SegmentController {

    final SegmentRepository segmentRepository;
    final StorageService storageService;
    final MemberRepository memberRepository;
    final UserDetailsProvider userDetailsProvider;
    final FileRepository fileRepository;
    final FolderRepository folderRepository;
    
    @GetMapping("/{fid}/{name}")
    public ResponseEntity<Resource> download(@PathVariable("fid") Long resourceId, @PathVariable("name") String name) throws Exception {
        List<Segment> segments = segmentRepository.findByFileIdAndName(resourceId, name);
        if (segments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "segment/notfound");
        } else {
            Segment segment = segments.get(0);
            String location = segment.getLocation();
            Long fileSize = segment.getSize();
            InputStream stream = storageService.downloadFile(location);
            HttpHeaders headers = new HttpHeaders();
            String mime = URLConnection.guessContentTypeFromName(name);
            if (mime == null) mime = "application/octet-stream";
            MediaType mediaType = MediaType.parseMediaType(mime);
            
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileSize)
                .contentType(mediaType)
                .body(new InputStreamResource(stream));
        }        
    }

    @PostMapping("/{fid}")
    public ResponseEntity<Void> upload(@PathVariable("fid") Long resourceId,
                                       @RequestParam("file") MultipartFile file)
        throws Exception {
        String fileName = file.getOriginalFilename();
        String mime = URLConnection.guessContentTypeFromName(fileName);
        if (mime == null) mime = "application/octet-stream";
        FileObjectDto fileDto
            = storageService.uploadFile(file.getInputStream(),
                                        mime, file.getSize());
        String location = fileDto.getObjectId();
        Optional<File> resourceFileOpt = fileRepository.findById(resourceId);
        File resourceFile = resourceFileOpt
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                           "segment/notfound"));
        Segment seg = Segment.builder()
            .file(resourceFile)
            .location(location)
            .name(fileName)
            .size(file.getSize())
            .build();
        segmentRepository.save(seg);
        return ResponseEntity.ok().build();
    }
    
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


    @PostMapping("/")
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
        String mimeType = "application/dash+xml";
        File file = File.of(folder, FileType.of(fileName, mimeType),
                            null, 0L, memberOpt.get());
        file = fileRepository.save(file);
        
        String contents = new String(formFile.getBytes(), StandardCharsets.UTF_8);
        String modified = insertBaseURL(contents, String.format("/segments/%d/", file.getId()));
        byte[] modifiedBytes = modified.getBytes();
        Long size = (long) modifiedBytes.length;
        InputStream stream = new ByteArrayInputStream(modifiedBytes);
        
        FileObjectDto fileObject = storageService
            .uploadFile(stream, mimeType, size);
        String location = fileObject.getObjectId();

        file.updateContents(location, size);
        fileRepository.save(file);
        
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

    private UserDetails getLoginUser() {
        return userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
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
