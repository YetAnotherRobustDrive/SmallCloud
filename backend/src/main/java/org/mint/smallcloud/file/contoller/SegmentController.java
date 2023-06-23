package org.mint.smallcloud.file.contoller;

import org.mint.smallcloud.bucket.dto.FileObjectDto;
import org.mint.smallcloud.bucket.service.StorageService;
import org.mint.smallcloud.exception.ExceptionStatus;
import org.mint.smallcloud.exception.ServiceException;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.domain.Segment;
import org.mint.smallcloud.file.repository.FileRepository;
import org.mint.smallcloud.file.repository.FolderRepository;
import org.mint.smallcloud.file.repository.IndexDataRepository;
import org.mint.smallcloud.file.repository.SegmentRepository;
import org.mint.smallcloud.file.service.SegmentService;
import org.mint.smallcloud.security.UserDetailsProvider;
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

import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/segments")
public class SegmentController {

    final SegmentRepository segmentRepository;
    final StorageService storageService;
    final MemberRepository memberRepository;
    final UserDetailsProvider userDetailsProvider;
    final FileRepository fileRepository;
    final FolderRepository folderRepository;
    final IndexDataRepository indexDataRepository;
    final SegmentService segmentService;

    public SegmentController(SegmentRepository segmentRepository, StorageService storageService, MemberRepository memberRepository, UserDetailsProvider userDetailsProvider, FileRepository fileRepository, FolderRepository folderRepository, IndexDataRepository indexDataRepository, SegmentService segmentService) {
        this.segmentRepository = segmentRepository;
        this.storageService = storageService;
        this.memberRepository = memberRepository;
        this.userDetailsProvider = userDetailsProvider;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.indexDataRepository = indexDataRepository;
        this.segmentService = segmentService;
    }

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

    public static class UploadResponse {
        Long id;
        String securityLevel;
        String writingStage;
        String name;
        Long size;
        String type;
        String thumbnail;
        boolean shared;

        UploadResponse(Long id, String securityLevel, String writingStage, String name, Long size, String type, String thumbnail, boolean shared) {
            this.id = id;
            this.securityLevel = securityLevel;
            this.writingStage = writingStage;
            this.name = name;
            this.size = size;
            this.type = type;
            this.thumbnail = thumbnail;
            this.shared = shared;
        }

        public static UploadResponseBuilder builder() {
            return new UploadResponseBuilder();
        }

        public Long getId() {
            return this.id;
        }

        public String getSecurityLevel() {
            return this.securityLevel;
        }

        public String getWritingStage() {
            return this.writingStage;
        }

        public String getName() {
            return this.name;
        }

        public Long getSize() {
            return this.size;
        }

        public String getType() {
            return this.type;
        }

        public String getThumbnail() {
            return this.thumbnail;
        }

        public boolean isShared() {
            return this.shared;
        }

        public static class UploadResponseBuilder {
            private Long id;
            private String securityLevel;
            private String writingStage;
            private String name;
            private Long size;
            private String type;
            private String thumbnail;
            private boolean shared;

            UploadResponseBuilder() {
            }

            public UploadResponseBuilder id(Long id) {
                this.id = id;
                return this;
            }

            public UploadResponseBuilder securityLevel(String securityLevel) {
                this.securityLevel = securityLevel;
                return this;
            }

            public UploadResponseBuilder writingStage(String writingStage) {
                this.writingStage = writingStage;
                return this;
            }

            public UploadResponseBuilder name(String name) {
                this.name = name;
                return this;
            }

            public UploadResponseBuilder size(Long size) {
                this.size = size;
                return this;
            }

            public UploadResponseBuilder type(String type) {
                this.type = type;
                return this;
            }

            public UploadResponseBuilder thumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
                return this;
            }

            public UploadResponseBuilder shared(boolean shared) {
                this.shared = shared;
                return this;
            }

            public UploadResponse build() {
                return new UploadResponse(this.id, this.securityLevel, this.writingStage, this.name, this.size, this.type, this.thumbnail, this.shared);
            }

            public String toString() {
                return "SegmentController.UploadResponse.UploadResponseBuilder(id=" + this.id + ", securityLevel=" + this.securityLevel + ", writingStage=" + this.writingStage + ", name=" + this.name + ", size=" + this.size + ", type=" + this.type + ", thumbnail=" + this.thumbnail + ", shared=" + this.shared + ")";
            }
        }
    }


    @PostMapping("/")
    public UploadResponse upload(
        @RequestParam("file") MultipartFile formFile,
        @RequestParam("originFileId") Long fileId
    ) {
        UserDetails user = getLoginUser();
        String userName = user.getUsername();
        return segmentService.createIndexData(userName, fileId, formFile);
    }

    private UserDetails getLoginUser() {
        return userDetailsProvider.getUserDetails()
            .orElseThrow(() -> new ServiceException(ExceptionStatus.NO_PERMISSION));
    }


}
