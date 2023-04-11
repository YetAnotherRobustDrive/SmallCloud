package org.mint.smallcloud.file.Controller;

import lombok.Getter;
import org.mint.smallcloud.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/file")
public class FileController {

    private  final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String selectFileMetadata(@PathVariable String)

}
